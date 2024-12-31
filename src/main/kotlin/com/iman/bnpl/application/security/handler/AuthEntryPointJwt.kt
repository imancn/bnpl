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
import java.util.*

@Component
class AuthEntryPointJwt(
    private val modelMapper: ObjectMapper,
    private val resourceBundle: ResourceBundle
) : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        val key = "invalid.credentials"
        response.writer.print(
            modelMapper.writeValueAsString(
                ErrorMessageResponse(key, resourceBundle.getString(key))
            )
        )
    }
}
