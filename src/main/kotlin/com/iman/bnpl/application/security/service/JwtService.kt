package com.iman.bnpl.application.security.service

import com.iman.bnpl.domain.user.data.model.UserDetailsImpl
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtService(
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    @Value("\${app.jwtSecret}")
    private val jwtSecretString: String? = null

    @Value("\${app.jwtExpirationMs}")
    private val jwtExpirationMs = 0
    
    private lateinit var jwtSecret: Key
    
    @PostConstruct
    fun init() {
        jwtSecret = Keys.hmacShaKeyFor(jwtSecretString!!.toByteArray())
    }
    
    fun generateJwtToken(userPrincipal: UserDetailsImpl): String {
        val roles = userPrincipal.authorities.map { it.authority }
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .claim("roles", roles)
            .claim("phoneNumber", userPrincipal.phoneNumber)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(jwtSecret)
            .compact()
    }

    fun getUserIdFromJwtToken(token: String?): String {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .body.subject
        } catch (e: SignatureException) {
            throw JwtException("Authorization JWT token is invalid: Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            throw JwtException("Authorization JWT token is invalid: Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            throw JwtException("Authorization JWT token is invalid: JWT token is expired")
        } catch (e: UnsupportedJwtException) {
            throw JwtException("Authorization JWT token is invalid: JWT token is unsupported")
        } catch (e: IllegalArgumentException) {
            throw JwtException("Authorization JWT token is invalid: JWT claims string is empty")
        } catch (ex: Exception) {
            throw JwtException("Authorization JWT token is invalid")
        }
    }

    fun parseJwt(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader.isNullOrEmpty())
            return null
        if (!authorizationHeader.startsWith("Bearer "))
            return null
        return authorizationHeader.substring(7, authorizationHeader.length)
    }
    
    fun getClaims(token: String): Claims? {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret)
            .build()
            .parseClaimsJws(token)
            .body
    }
    
    fun getAuthoritiesFromJwtToken(token: String): List<SimpleGrantedAuthority> {
        val claims = Jwts.parserBuilder()
            .setSigningKey(jwtSecret)
            .build()
            .parseClaimsJws(token)
            .body
        return claims.get("roles", List::class.java).map {
            SimpleGrantedAuthority(it.toString())
        }
    }
    
    fun generateTokenFromUserId(userId: String): String {
        return generateJwtToken(userDetailsServiceImpl.loadUserByUsername(userId))
    }
}