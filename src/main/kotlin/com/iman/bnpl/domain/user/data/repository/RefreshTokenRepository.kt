package com.iman.bnpl.domain.user.data.repository

import com.iman.bnpl.domain.user.data.model.RefreshTokenEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshTokenEntity, String> {
    fun deleteByUserId(userId: String)
}