package com.iman.bnpl.application.advice

import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    @ExceptionHandler(value = [HttpException::class])
    fun handleHttpException(ex: HttpException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString())
        return ResponseEntity(
            ErrorMessageResponse(ex.message ?: ""), ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [LogicalException::class])
    fun handleLogicalException(ex: LogicalException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString(), ex.message)
        return ResponseEntity(
            ErrorMessageResponse(ex.message ?: ""), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
    
    @ExceptionHandler(value = [UnprocessableException::class])
    fun handleHttpException(ex: UnprocessableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(ErrorMessageResponse("${ex.message}"), ex.httpStatus)
    }
    
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse(ex.bindingResult.allErrors.first().defaultMessage ?: ex.message), HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse(ex.message ?: ""), HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        val message = (ex).constraintViolations.first().messageTemplate
        return ResponseEntity(
            ErrorMessageResponse(message), HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageConversionException(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse("Malformed JSON body"), HttpStatus.BAD_REQUEST
        )
    }
}