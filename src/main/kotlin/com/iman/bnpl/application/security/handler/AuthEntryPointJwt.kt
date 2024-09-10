package com.iman.bnpl.application.security.handler

import com.iman.bnpl.application.advice.ErrorMessageResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPointJwt(
    private val modelMapper: ObjectMapper
) : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.print(
            modelMapper.writeValueAsString(
                ErrorMessageResponse("Invalid or expired JWT token")
            )
        )
    }
}
