package com.iman.bnpl.domain.user.services

import com.iman.bnpl.application.advice.UnprocessableException
import com.iman.bnpl.domain.user.data.model.RefreshTokenEntity
import com.iman.bnpl.domain.user.data.repository.RefreshTokenRepository
import com.iman.bnpl.domain.user.data.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val userRepository: UserRepository,
) {
    @Value("\${app.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs: Long? = null

    fun findByToken(token: String): Optional<RefreshTokenEntity> {
        return refreshTokenRepository.findById(token)
    }

    fun createRefreshToken(userId: String): RefreshTokenEntity {
        var refreshToken = RefreshTokenEntity(
            null,
            userId,
            Date(System.currentTimeMillis() + refreshTokenDurationMs!!)
        )
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun deleteByUserId(userId: String) {
        if (!userRepository.existsByIdAndDeleted(userId)) { throw UnprocessableException("User Not Found") }
        refreshTokenRepository.deleteByUserId(userId)
    }
    
    fun deleteById(id: String) {
        refreshTokenRepository.deleteById(id)
    }
}