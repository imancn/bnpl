package com.iman.bnpl.application.advice

import org.springframework.http.HttpStatus

sealed class HttpException(val httpStatus: HttpStatus, val key: String): RuntimeException()

class InvalidCredentialException(key: String = "invalid.credentials") : HttpException(HttpStatus.UNAUTHORIZED, key)

class AccessDeniedException(key: String = "access.denied") : HttpException(HttpStatus.FORBIDDEN, key)

class InvalidInputException(key: String = "invalid.input") : HttpException(HttpStatus.BAD_REQUEST, key)

class UnprocessableException(key: String = "unprocessable"): HttpException(HttpStatus.UNPROCESSABLE_ENTITY, key)

class InternalServerError(key: String = "internal.server.error") : HttpException(HttpStatus.INTERNAL_SERVER_ERROR, key)