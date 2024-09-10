package com.iman.bnpl.application.shared.util

import io.jsonwebtoken.Claims
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object Auth {
    fun userId(): String {
        return getAuthentication()?.principal?.let {
            if (it is String) it else null
        } ?: ""
    }
    
    fun userPhoneNumber(): String {
        return getClaims()?.get("phoneNumber", String::class.java) ?: ""
    }
    
    fun isAdmin() = hasAuthority("ROLE_ADMIN")
    
    fun isCustomer() = hasAuthority("ROLE_CUSTOMER")
    
    private fun hasAuthority(role: String): Boolean {
        return getAuthentication()?.authorities?.contains(SimpleGrantedAuthority(role)) ?: false
    }
    
    private fun getClaims(): Claims? {
        return getAuthentication()?.credentials?.let {
            if (it is Claims) return it else null
        }
    }
    
    private fun getAuthentication(): Authentication? = SecurityContextHolder.getContext().authentication
}