package com.iman.bnpl.domain.user.services

import com.iman.bnpl.actor.http.user.payload.response.JwtResponse
import com.iman.bnpl.actor.http.user.payload.response.RefreshTokenResponse
import com.iman.bnpl.application.advice.*
import com.iman.bnpl.application.security.service.JwtService
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.user.data.model.OtpType
import com.iman.bnpl.domain.user.data.model.UserDetailsImpl
import com.iman.bnpl.domain.user.data.model.UserEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class AuthService(
    private val userService: UserService,
    private val otpService: OtpService,
    private val jwtService: JwtService,
    private val refreshTokenService: RefreshTokenService,
    private val authenticationManager: AuthenticationManager,
) {
    fun loginUser(
        phoneNumber: String,
        password: String
    ): JwtResponse {
        val user = userService.getUserByPhoneNumber(phoneNumber.validatePhoneNumber()).orElseThrow {
            InvalidCredentialException()
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.id, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtToken(authentication.principal as UserDetailsImpl)
        val userDetails = authentication.principal as UserDetailsImpl
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return JwtResponse(
            jwt, refreshToken.id ?: "", userDetails.phoneNumber
        )
    }
    
    fun verifyOtpToken(
        phoneNumber: String,
        otp: String
    ): JwtResponse {
        val user = userService.getUserByPhoneNumber(phoneNumber.validatePhoneNumber()).orElseThrow {
            UnprocessableException("You are not registered")
        }
        otpService.validateOtpTokenForLogin(user.id ?: "", otp, OtpType.LOGIN)
        val userDetails = UserDetailsImpl(
            user.id ?: "",
            user.phoneNumber,
            user.password,
            user.roles.map { role -> SimpleGrantedAuthority(role.name) },
            user.deleted
        )
        val jwt = jwtService.generateJwtToken(userDetails)
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return JwtResponse(
            jwt, refreshToken.id ?: "", userDetails.phoneNumber
        )
    }
    
    fun loginOrRegisterUser(phoneNumber: String, fullName: String? = null, password: String? = null): UserEntity {
        val user = userService.getUserByPhoneNumber(phoneNumber.validatePhoneNumber()).getOrElse {
            userService.registerUser(phoneNumber, fullName ?: phoneNumber, password)
        }
        otpService.sendLoginOtp(user.id ?: "", phoneNumber.validatePhoneNumber())
        return user
    }
    
    fun registerUser(phoneNumber: String, fullName: String? = null, password: String? = null): UserEntity {
        val user = userService.registerUser(phoneNumber.validatePhoneNumber(), fullName ?: phoneNumber, password)
        otpService.sendLoginOtp(user.id ?: "", phoneNumber.validatePhoneNumber())
        return user
    }
    
    fun refreshToken(refreshToken: String): RefreshTokenResponse {
        return refreshTokenService.findByToken(refreshToken).map {
            val token = jwtService.generateTokenFromUserId(it.userId)
            val newRefreshToken = refreshTokenService.createRefreshToken(it.userId)
            refreshTokenService.deleteById(refreshToken)
            RefreshTokenResponse(token, newRefreshToken.id ?: "")
        }.orElseThrow {
            UnprocessableException("Your Session has been expired")
        }
    }
    
    fun changeUserPassword(oldPassword: String, newPassword: String) {
        userService.changePassword(Auth.userId(), oldPassword, newPassword)
    }
    
    fun deleteRefreshToken() {
        refreshTokenService.deleteByUserId(Auth.userId())
    }
    
    private fun String.validatePhoneNumber(): String {
        val matchedByRegex = Regex("^(?!\\+98)9\\d{9}$").containsMatchIn(this)
        if (!matchedByRegex || !this.startsWith("9") || this.length != 10) {
            throw InvalidInputException("Invalid phone number")
        }
        return this
    }
}