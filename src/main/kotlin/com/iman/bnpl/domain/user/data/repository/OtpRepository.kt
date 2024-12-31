package com.iman.bnpl.domain.user.data.repository

import com.iman.bnpl.domain.user.data.model.OtpEntity
import com.iman.bnpl.domain.user.data.model.OtpType
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface OtpRepository : MongoRepository<OtpEntity, String> {
    fun findByUserId(userId: String): Optional<OtpEntity>
    fun deleteByUserIdAndType(userId: String, type: OtpType)
}