package io.github.eaxdev.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.eaxdev.model.Loan
import io.github.eaxdev.model.Person
import io.github.eaxdev.repository.LoanRepository
import io.github.eaxdev.repository.PersonRepository
import org.springframework.stereotype.Service
import java.io.File
import java.time.format.DateTimeFormatter

private data class PersonDto(
    @JsonProperty("ID")
    val id: String,
    @JsonProperty("DocId")
    val docId: String,
    @JsonProperty("FIO")
    val fio: String,
    @JsonProperty("Birthday")
    val birthday: String,
    @JsonProperty("Salary")
    val salary: String,
    @JsonProperty("Gender")
    val gender: String
)

private data class PersonFile(
    val persons: List<PersonDto>
)

@Service
class PersonService(
    val objectMapper: ObjectMapper,
    val personRepository: PersonRepository) {

    private val srcDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
    private val dstDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun load() {
        val uri = ClassLoader.getSystemResource("persons.json").toURI()
        val file = File(uri)
        val persons = objectMapper.readValue(file, PersonFile::class.java).persons
        personRepository.saveAll(persons.map { it.toPerson() })
    }

    private fun PersonDto.toPerson(): Person {
        return Person(
            id = id,
            docid = docId,
            fio = fio,
            birthday = convertDate(birthday),
            salary = salary.toDouble() * 100,
            gender = gender
        )
    }

    private fun convertDate(date: String) : String {
        val parsed = srcDateFormatter.parse(date)
        return dstDateFormatter.format(parsed)
    }

    fun getPerson(documentId: String): Person {
        return personRepository.findByDocid(documentId) ?: throw PersonNotFoundException()
    }

}

class PersonNotFoundException : Exception()