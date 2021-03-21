package io.github.eaxdev.dto

import io.github.eaxdev.model.Loan
import io.github.eaxdev.model.Person
import java.math.BigDecimal


data class PersonDto(
    val docid: String,
    val fio: String,
    val birthday: String,
    val salary: BigDecimal,
    val gender: String
) {
    companion object {
        fun of(person: Person): PersonDto {
            return PersonDto(
                docid = person.docid,
                fio = person.fio,
                birthday = person.birthday,
                salary = person.salary,
                gender = person.gender
            )
        }
    }
}

data class PersonWithLoansDto(
    val id: String,
    val docid: String,
    val fio: String,
    val birthday: String,
    val salary: BigDecimal,
    val gender: String,
    val loans: List<LoanDto>
) {
    companion object {
        fun of(person: Person, loans: List<Loan>): PersonWithLoansDto {
            return PersonWithLoansDto(
                id = person.id,
                docid = person.docid,
                fio = person.fio,
                birthday = person.birthday,
                salary = person.salary,
                gender = person.gender,
                loans = loans.map { loan -> LoanDto.of(loan) }
            )
        }
    }
}