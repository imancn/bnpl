package com.iman.bnpl.domain.user.data.repository

import com.iman.bnpl.domain.user.data.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<UserEntity, String> {
    fun findByPhoneNumberAndDeleted(phoneNumber: String, deleted: Boolean = false): Optional<UserEntity>
    fun findByIdAndDeleted(userId: String, deleted: Boolean = false): Optional<UserEntity>
    fun existsByIdAndDeleted(userId: String, deleted: Boolean = false): Boolean
}