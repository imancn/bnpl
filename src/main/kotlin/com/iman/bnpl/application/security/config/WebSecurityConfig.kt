package com.iman.bnpl.application.security.config

import com.iman.bnpl.application.security.filter.AuthTokenFilter
import com.iman.bnpl.application.security.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    private val jwtService: JwtService,
    private val unauthorizedHandler: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/v3/api-docs/**").permitAll()
                authorize.requestMatchers("/swagger-ui.html").permitAll()
                authorize.requestMatchers("/swagger-ui/**").permitAll()
                
                authorize.requestMatchers("/error").permitAll()
                
                authorize.requestMatchers("/actuator/**").permitAll()
                
                authorize.requestMatchers("/api/public/**").permitAll()
                authorize.requestMatchers("/api/auth/**").permitAll()
                authorize.requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                authorize.requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                authorize.anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(AuthTokenFilter(jwtService), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().also { cors ->
        CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("POST", "GET")
            allowedHeaders = listOf(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )
            exposedHeaders = listOf(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"
            )
            maxAge = 3600
            cors.registerCorsConfiguration("/**", this)
        }
    }
}