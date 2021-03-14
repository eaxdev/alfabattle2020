package io.github.eaxdev.controller

import io.github.eaxdev.service.LoansService
import io.github.eaxdev.service.PersonNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(PersonNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePersonNotFound(e: PersonNotFoundException): ErrorDto {
        return ErrorDto("person not found")
    }

    @ExceptionHandler(LoansService.LoanNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleLoanNotFound(e: PersonNotFoundException): ErrorDto {
        return ErrorDto("loan not found")
    }

}

data class ErrorDto(val status: String)