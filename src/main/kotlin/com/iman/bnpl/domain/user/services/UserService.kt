package com.iman.bnpl.domain.user.services

import com.iman.bnpl.actor.http.user.payload.UpdateUserProfileRequest
import com.iman.bnpl.application.advice.InvalidInputException
import com.iman.bnpl.application.advice.UnprocessableException
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.user.data.model.Role
import com.iman.bnpl.domain.user.data.model.UserEntity
import com.iman.bnpl.domain.user.data.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val repository: UserRepository,
    private val encoder: PasswordEncoder,
) {
    fun getUserByPhoneNumber(phoneNumber: String): Optional<UserEntity> {
        return repository.findByPhoneNumberAndDeleted(phoneNumber)
    }
    
    fun getUserById(userId: String): UserEntity {
        return repository.findById(userId).orElseThrow {
            UnprocessableException("User not found")
        }
    }
    
    fun updateUserProfile(request: UpdateUserProfileRequest): UserEntity {
        val user = getUserById(Auth.userId())
        user.fullName = request.fullName
        return repository.save(user)
    }
    
    fun registerUser(phoneNumber: String, fullName: String, password: String?): UserEntity {
        return repository.save(
            UserEntity(
                id = null,
                fullName = fullName,
                phoneNumber = phoneNumber,
                password = password?.let { encoder.encode(it) },
                roles = setOf(Role.ROLE_CUSTOMER)
            )
        )
    }
    
    fun changePassword(userId: String, oldPassword: String, newPassword: String) {
        val user = getUserById(userId)
        if (user.password == encoder.encode(oldPassword)) {
            user.password = encoder.encode(newPassword)
            repository.save(user)
        }
        else throw InvalidInputException("Old Password is not correct.")
    }
}