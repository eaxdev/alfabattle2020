package io.github.eaxdev.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.eaxdev.model.Loan
import io.github.eaxdev.repository.LoanRepository
import io.github.eaxdev.repository.PersonRepository
import org.springframework.stereotype.Service
import java.io.File
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
    val loanRepository: LoanRepository
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
            amount = amount.toDouble() * 100,
            startdate = convertDate(startDate),
            period = period.toInt() * 12
        )
    }

    private fun convertDate(date: String) : String {
        val parsed = srcDateFormatter.parse(date)
        return dstDateFormatter.format(parsed)
    }

    fun getLoan(loan: String): Loan {
        return loanRepository.findById(loan).orElseThrow { throw LoanNotFoundException() }
    }

    fun loadHistory(documentId: String): List<Loan> {
        return loanRepository.findAllByDocid(documentId)
    }

    class LoanNotFoundException : Exception()
}
