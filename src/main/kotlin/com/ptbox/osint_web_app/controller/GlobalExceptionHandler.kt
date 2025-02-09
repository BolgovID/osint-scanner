package com.ptbox.osint_web_app.controller

import com.ptbox.osint_web_app.exception.DomainAlreadyScanningException
import com.ptbox.osint_web_app.exception.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val fieldErrors: List<FieldError> = ex.bindingResult.fieldErrors
        val errorMessage = fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        val errorResponse = ErrorResponse(
            code = NOT_VALID,
            message = "Validation failed",
            details = errorMessage
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = ENTITY_NOT_FOUND,
            message = ex.message ?: "Entity not found",
            details = "The requested entity could not be found"
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DomainAlreadyScanningException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDomainAlreadyScanningException(ex: DomainAlreadyScanningException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = DOMAIN_ALREADY_SCANNING,
            message = ex.message ?: "The domain is already being scanned",
            details = "The provided domain is currently being scanned and cannot be processed"
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = INTERNAL_SERVER_ERROR,
            message = ex.message ?: "An unexpected error occurred",
            details = "Please try again later"
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    data class ErrorResponse(
        val code: String,
        val message: String,
        val details: String? = null
    )

    companion object {
        const val NOT_VALID = "INVALID_DATA"
        const val ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND"
        const val DOMAIN_ALREADY_SCANNING = "DOMAIN_ALREADY_SCANNING"
        const val INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR"
    }
}