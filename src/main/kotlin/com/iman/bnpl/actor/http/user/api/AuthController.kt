package com.iman.bnpl.actor.http.user.api

import com.iman.bnpl.actor.http.user.payload.response.JwtResponse
import com.iman.bnpl.actor.http.user.payload.response.RefreshTokenResponse
import com.iman.bnpl.application.advice.*
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.user.data.repository.OtpTokenRepository
import com.iman.bnpl.domain.user.data.repository.UserRepository
import com.iman.bnpl.application.security.service.JwtService
import com.iman.bnpl.domain.user.services.RefreshTokenService
import com.iman.bnpl.domain.user.data.model.Role
import com.iman.bnpl.domain.user.data.model.UserDetailsImpl
import com.iman.bnpl.domain.user.data.model.UserEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
@Validated
class AuthController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val refreshTokenService: RefreshTokenService,
    val otpTokenRepository: OtpTokenRepository,
    val encoder: PasswordEncoder,
    val jwtService: JwtService,
) {
//    @Todo: Move the logic to the service layer
    
    @PostMapping("/sign-in/otp/verify")
    fun verifyOtpToSignInUser(
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}\\b\n") phoneNumber: String,
        @RequestParam @NotBlank otp: String
    ): JwtResponse {
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber).orElseThrow {
            UnprocessableException("You are not registered")
        }
        otpTokenRepository.findByUserId(user.id ?: "").let {
            if (!it.isPresent || otp != it.get().token) {
                throw AccessDeniedException("Invalid credentials")
            }
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.id, null)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtToken(authentication.principal as UserDetailsImpl)
        val userDetails = authentication.principal as UserDetailsImpl
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return JwtResponse(
            jwt, refreshToken.id ?: "", userDetails.phoneNumber
        )
    }
    
    @PostMapping("/sign-in/otp/request")
    fun requestOtpToSignInUserUser(
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}\\b\n") phoneNumber: String,
    ) {
        userRepository.findByPhoneNumberAndDeleted(phoneNumber).orElseThrow {
            UnprocessableException("You are not registered")
        }
        sendOtp(phoneNumber)
    }
    
    @PostMapping("/customer/signup")
    fun registerStudent(
        @RequestParam @NotBlank fullName: String,
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}\\b\n") phoneNumber: String,
        @RequestParam @Size(min = 6, max = 40) password: String?,
    ) {
        if (!Regex("\\b9\\d{9}\\b").containsMatchIn(phoneNumber))
            throw InvalidInputException("Invalid phone number")
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber)
        if (!user.isPresent) {
            userRepository.save(
                UserEntity(
                    id = null,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    password = password?.let{ encoder.encode(it) },
                    roles = setOf(Role.ROLE_CUSTOMER)
                )
            )
        }
        sendOtp(phoneNumber)
    }
    
    private fun sendOtp(phoneNumber: String) {
        TODO("Not yet implemented")
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam @NotBlank refreshToken: String,
    ): RefreshTokenResponse {
        return refreshTokenService.findByToken(refreshToken).map {
            val token = jwtService.generateTokenFromUserId(it.userId)
            val newRefreshToken = refreshTokenService.createRefreshToken(it.userId)
            refreshTokenService.deleteById(refreshToken)
            RefreshTokenResponse(token, newRefreshToken.id ?: "")
        }.orElseThrow {
            RefreshTokenException("Your Session has been expired")
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    fun changePassword(
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): String {
        val user = userRepository.findByIdAndDeleted(Auth.userId()).orElseThrow { NotFoundException("User Not Found") }
        if (user.password == encoder.encode(oldPassword)) user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")
        return "Your password has been changed."
    }
    
    @PostMapping("/sign-out")
    fun signOutUser(): String {
        refreshTokenService.deleteByUserId(Auth.userId())
        return "User signed out successfully"
    }
}