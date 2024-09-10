package com.iman.bnpl.application.security.service

import com.iman.bnpl.application.advice.InvalidTokenException
import com.iman.bnpl.domain.user.data.model.UserDetailsImpl
import com.iman.bnpl.domain.user.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    private var userRepository: UserRepository
) : UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetailsImpl {
        return UserDetailsImpl.build(
            userRepository.findByIdAndDeleted(userId).orElseThrow { InvalidTokenException("User Not Found") }
        )
    }

    fun userExist(userId: String): Boolean {
        return userRepository.existsByIdAndDeleted(userId)
    }
}