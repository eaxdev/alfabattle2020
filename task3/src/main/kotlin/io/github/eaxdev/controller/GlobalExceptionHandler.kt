package io.github.eaxdev.controller

import io.github.eaxdev.dto.response.ErrorResponse
import io.github.eaxdev.service.BranchNotFound
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BranchNotFound::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleAtmNotFound(): ErrorResponse {
        return ErrorResponse("branch not found")
    }

}