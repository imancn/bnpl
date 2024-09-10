package com.iman.bnpl.application.security.filter

import com.iman.bnpl.application.security.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthTokenFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        try {
            val jwt = jwtService.parseJwt(request)
            if (jwt != null) {
                val userId = jwtService.getUserIdFromJwtToken(jwt)
                val authentication = UsernamePasswordAuthenticationToken(
                    userId, null, jwtService.getAuthoritiesFromJwtToken(jwt)
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            SecurityContextHolder.getContext().authentication = null
        }
        filterChain.doFilter(request, response)
    }
}