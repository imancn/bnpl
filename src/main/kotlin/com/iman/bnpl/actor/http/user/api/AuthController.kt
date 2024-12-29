package com.iman.bnpl.actor.http.user.api

import com.iman.bnpl.actor.http.user.payload.response.JwtResponse
import com.iman.bnpl.actor.http.user.payload.response.RefreshTokenResponse
import com.iman.bnpl.actor.shared.model.response.MessageResponse
import com.iman.bnpl.domain.user.services.AuthService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
@Validated
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun loginUser(
        @RequestParam(required = false)
        @NotBlank(message = "Phone number is blank")
        phoneNumber: String,
        
        @RequestParam(required = false)
        @NotBlank(message = "Password is blank")
        password: String
    ): JwtResponse {
        return authService.loginUser(phoneNumber, password)
    }
    
    @PostMapping("/login/otp/verify")
    fun verifyOtpToLoginUser(
        @RequestParam(required = false)
        @NotBlank(message = "Phone number is blank")
        phoneNumber: String,
        
        @RequestParam(required = false)
        @NotBlank(message = "OTP is blank")
        otp: String
    ): JwtResponse {
        return authService.verifyOtpToken(phoneNumber, otp)
    }
    
    @PostMapping("/login/otp/request")
    fun requestOtpToLoginUserUser(
        @RequestParam(required = false)
        @NotBlank(message = "Phone number is blank")
        phoneNumber: String,
    ) {
        authService.loginOrRegisterUser(phoneNumber)
    }
    
    @PostMapping("/customer/register")
    fun registerStudent(
        @RequestParam(required = false)
        @NotBlank(message = "Name is blank")
        fullName: String,
        
        @RequestParam(required = false)
        @NotBlank(message = "Phone number is blank")
        phoneNumber: String,
        
        @RequestParam(required = false)
        @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
        @NotBlank(message = "Password is blank")
        password: String,
    ) {
        authService.registerUser(phoneNumber, fullName, password)
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam(required = false)
        @NotBlank(message = "refresh token is blank")
        refreshToken: String,
    ): RefreshTokenResponse {
        return authService.refreshToken(refreshToken)
    }
    
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun changePassword(
        @RequestParam(required = false)
        @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
        @NotBlank(message = "Password is blank")
        oldPassword: String,
        
        @RequestParam(required = false)
        @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
        @NotBlank(message = "Password is blank")
        newPassword: String
    ): MessageResponse {
        authService.changeUserPassword(oldPassword, newPassword)
        return MessageResponse("Your password has been changed.")
    }
    
    @PostMapping("/logout")
    fun logoutUser(): MessageResponse {
        authService.deleteRefreshToken()
        return MessageResponse("User signed out successfully")
    }
}