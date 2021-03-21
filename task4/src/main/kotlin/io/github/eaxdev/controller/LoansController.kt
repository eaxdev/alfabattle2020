package io.github.eaxdev.controller

import io.github.eaxdev.dto.*
import io.github.eaxdev.service.LoansService
import io.github.eaxdev.service.PersonService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoansController(val loansService: LoansService, val personService: PersonService) {

    @GetMapping("/loadLoans")
    fun loadLoans() : ResponseEntity<LoadResultDto> {
        loansService.load()
        return ResponseEntity.ok(LoadResultDto())
    }

    @GetMapping("/loadPersons")
    fun loadPersons() : ResponseEntity<LoadResultDto> {
        personService.load()
        return ResponseEntity.ok(LoadResultDto())
    }

    @GetMapping("/getPerson/{documentId}")
    fun getPerson(@PathVariable("documentId") documentId: String) : ResponseEntity<PersonDto> {
        return ResponseEntity.ok(PersonDto.of(personService.getPerson(documentId)))
    }

    @GetMapping("/getLoan/{loan}")
    fun getLoan(@PathVariable("loan") loan: String) : ResponseEntity<LoanDto> {
        return ResponseEntity.ok(LoanDto.of(loansService.getLoan(loan)))
    }

    @GetMapping("/creditHistory/{documentId}")
    fun loadHistory(@PathVariable("documentId") documentId: String) : ResponseEntity<LoanHistoryDto> {
        return ResponseEntity.ok(LoanHistoryDto.of(loansService.findAllByDocId(documentId)))
    }

    @GetMapping("/creditClosed")
    fun creditClosed(pageable: Pageable) : ResponseEntity<Page<LoanDto>> {
        return ResponseEntity.ok(loansService.loansClosed(pageable).map { LoanDto.of(it) })
    }

    @GetMapping("/loansSortByPersonBirthday")
    fun loansSortByPersonBirthday(pageable: Pageable) : ResponseEntity<Page<PersonWithLoansDto>> {
        val persons = personService.findAllSortByBirthday(pageable)

        val result = persons.map {
            val loans = loansService.findAllByDocId(it.docid)
            PersonWithLoansDto.of(it, loans)
        }

        return ResponseEntity.ok(result)
    }
}