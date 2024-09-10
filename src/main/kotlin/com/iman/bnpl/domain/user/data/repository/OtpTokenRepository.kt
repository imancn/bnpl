package com.iman.bnpl.domain.user.data.repository

import com.iman.bnpl.domain.user.data.model.OtpTokenEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface OtpTokenRepository : MongoRepository<OtpTokenEntity, String> {
    fun findByUserId(userId: String): Optional<OtpTokenEntity>
}