package com.iman.bnpl.domain.user.services

import com.iman.bnpl.application.advice.UnprocessableException
import com.iman.bnpl.domain.user.data.model.UserEntity
import com.iman.bnpl.domain.user.data.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository
) {
    fun getUserByPhoneNumber(phoneNumber: String): UserEntity {
        return repository.findByPhoneNumberAndDeleted(phoneNumber)
            .orElseThrow { UnprocessableException("User Not Found") }
    }
}