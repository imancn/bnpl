package com.iman.bnpl.application.security.handler

import com.iman.bnpl.application.advice.ErrorMessageResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val modelMapper: ObjectMapper
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        response.writer.print(modelMapper.writeValueAsString(ErrorMessageResponse(accessDeniedException.message ?: "")))
    }
}