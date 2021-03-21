package io.github.eaxdev.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.eaxdev.dto.PersonWithLoansDto
import io.github.eaxdev.model.Loan
import io.github.eaxdev.repository.LoanRepository
import io.github.eaxdev.repository.PersonRepository
import org.springframework.data.domain.*
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.StringQuery
import org.springframework.stereotype.Service
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private data class LoanDto(
    @JsonProperty("Loan")
    val loan: String,
    @JsonProperty("PersonId")
    val personId: String,
    @JsonProperty("Amount")
    val amount: String,
    @JsonProperty("StartDate")
    val startDate: String,
    @JsonProperty("Period")
    val period: String
)

private data class LoanFile(
    val loans: List<LoanDto>
)

@Service
class LoansService(
    val objectMapper: ObjectMapper,
    val personRepository: PersonRepository,
    val loanRepository: LoanRepository,
    val operations: ElasticsearchOperations
) {

    private val srcDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
    private val dstDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun load() {
        val uri = ClassLoader.getSystemResource("loans.json").toURI()
        val file = File(uri)
        val loans = objectMapper.readValue(file, LoanFile::class.java).loans
        loanRepository.saveAll(loans.map { it.toLoan() })
    }

    private fun LoanDto.toLoan(): Loan {
        val document = personRepository.findById(personId).get().docid

        return Loan(
            loan = loan,
            docid = document,
            amount = amount.toBigDecimal().multiply(BigDecimal(100)),
            startdate = convertDate(startDate),
            period = period.toInt() * 12
        )
    }

    private fun convertDate(date: String): LocalDate {
        val parsed = srcDateFormatter.parse(date)
        return LocalDate.from(parsed)
    }

    fun getLoan(loan: String): Loan {
        return loanRepository.findById(loan).orElseThrow { throw LoanNotFoundException() }
    }

    fun findAllByDocId(documentId: String): List<Loan> {
        return loanRepository.findAllByDocid(documentId)
    }

    fun loansClosed(pageable: Pageable): Page<Loan> {
        val pageableCustom = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            pageable.getSortOr(Sort.by(Sort.Order.desc( "startdate")))
        )

        val query = StringQuery(
            "{\n" +
                    "    \"script\": {\n" +
                    "      \"script\": {\n" +
                    "        \"source\": \"doc['startdate'].value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(doc['period'].value).compareTo(ZonedDateTime.ofInstant( Instant.ofEpochMilli(new Date().getTime()), ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1)) < 0\",\n" +
                    "        \"lang\": \"painless\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "}", pageableCustom
        )

        val loans = operations.search(query, Loan::class.java)
        return PageImpl(loans.searchHits.map { it.content }, pageableCustom, loans.totalHits)
    }

    fun getLoansSortByPersonBirthday(pageable: Pageable): Page<PersonWithLoansDto> {
        TODO("Not yet implemented")
    }

    class LoanNotFoundException : Exception()
}
