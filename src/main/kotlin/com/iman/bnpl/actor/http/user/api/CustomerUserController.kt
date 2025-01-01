package com.iman.bnpl.actor.http.user.api

import com.iman.bnpl.actor.http.user.payload.UpdateUserProfileRequest
import com.iman.bnpl.actor.http.user.payload.UserProfileResponse
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.user.services.UserService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer/v1")
@Validated
class CustomerUserController(
    private val userService: UserService,
) {
    @GetMapping("/profile")
    fun getUserProfile(): UserProfileResponse {
        return userService.getUserById(Auth.userId()).let {
            UserProfileResponse(
                it.firstName,
                it.lastName,
                it.phoneNumber,
            )
        }
    }
    
    @PostMapping("/profile")
    fun updateUserProfile(
        @RequestBody @Valid request: UpdateUserProfileRequest
    ): UserProfileResponse {
        return userService.updateUserProfile(request).let {
            UserProfileResponse(
                it.firstName,
                it.lastName,
                it.phoneNumber,
            )
        }
        
    }
}