package com.iman.bnpl.application.advice

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.*

@RestControllerAdvice
class ControllerAdviceHttp(
    private val bundle: ResourceBundle
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    @ExceptionHandler(value = [InvalidCredentialException::class])
    fun handleInvalidTokenException(ex: InvalidCredentialException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex, "invalid.credentials")
    }
    
    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex, "access.denied")
    }
    
    @ExceptionHandler(value = [InvalidInputException::class])
    fun handleInternalServerError(ex: InvalidInputException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex, "invalid.input")
    }
    
    @ExceptionHandler(value = [InternalServerError::class])
    fun handleInternalServerError(ex: InternalServerError, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex, "unprocessable")
    }
    
    @ExceptionHandler(value = [UnprocessableException::class])
    fun handleUnprocessableEntityException(ex: UnprocessableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return getErrorMessageResponse(ex, "internal.server.error")
    }
    
    private fun getErrorMessageResponse(ex: HttpException, defaultKey: String = "error"): ResponseEntity<ErrorMessageResponse> {
        val message = try { bundle.getString(ex.key) } catch (_: Exception) { bundle.getString(defaultKey) }
        return ResponseEntity(
            ErrorMessageResponse(ex.key, message), ex.httpStatus
        )
    }
}