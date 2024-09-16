package com.iman.bnpl.actor.http.user.api

import com.iman.bnpl.actor.http.user.payload.response.JwtResponse
import com.iman.bnpl.actor.http.user.payload.response.RefreshTokenResponse
import com.iman.bnpl.application.advice.*
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.user.data.repository.UserRepository
import com.iman.bnpl.application.security.service.JwtService
import com.iman.bnpl.domain.user.services.RefreshTokenService
import com.iman.bnpl.domain.user.data.model.Role
import com.iman.bnpl.domain.user.data.model.UserDetailsImpl
import com.iman.bnpl.domain.user.data.model.UserEntity
import com.iman.bnpl.domain.user.services.OtpTokenService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
    val encoder: PasswordEncoder,
    val jwtService: JwtService,
    private val otpTokenService: OtpTokenService,
) {
    @PostMapping("/sign-in")
    fun signInUser(
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}") phoneNumber: String,
        @RequestParam @NotBlank password: String
    ): JwtResponse {
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber).orElseThrow {
            AccessDeniedException("Invalid credentials")
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
    
    @PostMapping("/sign-in/otp/verify")
    fun verifyOtpToSignInUser(
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}") phoneNumber: String,
        @RequestParam @NotBlank otp: String
    ): JwtResponse {
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber).orElseThrow {
            UnprocessableException("You are not registered")
        }
        otpTokenService.validateOtpTokenForLogin(user.id ?: "", otp)
        val userDetails = UserDetailsImpl(
            user.id ?: "",
            user.phoneNumber,
            user.password,
            user.roles.map { role -> SimpleGrantedAuthority(role.name)},
            user.deleted
        )
        val jwt = jwtService.generateJwtToken(userDetails)
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return JwtResponse(
            jwt, refreshToken.id ?: "", userDetails.phoneNumber
        )
    }
    
    @PostMapping("/sign-in/otp/request")
    fun requestOtpToSignInUserUser(
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}") phoneNumber: String,
    ) {
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber).orElseThrow {
            UnprocessableException("You are not registered")
        }
        otpTokenService.sendOtp(user.id ?: "", phoneNumber)
    }
    
    @PostMapping("/customer/signup")
    fun registerStudent(
        @RequestParam @NotBlank fullName: String,
        @RequestParam @NotBlank @Pattern(regexp = "\\b9\\d{9}") phoneNumber: String,
        @RequestParam @Size(min = 6, max = 40) password: String?,
    ) {
        if (!Regex("\\b9\\d{9}").containsMatchIn(phoneNumber))
            throw InvalidInputException("Invalid phone number")
        val user = userRepository.findByPhoneNumberAndDeleted(phoneNumber).let {
            if (!it.isPresent) {
                userRepository.save(
                    UserEntity(
                        id = null,
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        password = password?.let{ password -> encoder.encode(password) },
                        roles = setOf(Role.ROLE_CUSTOMER)
                    )
                )
            } else it.get()
        }
        
        otpTokenService.sendOtp(user.id ?: "", phoneNumber)
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
    @PreAuthorize("hasAnyRole('ADMIN')")
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