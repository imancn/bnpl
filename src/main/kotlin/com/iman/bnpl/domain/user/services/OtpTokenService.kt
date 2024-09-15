package com.iman.bnpl.domain.user.services

import com.iman.bnpl.application.advice.AccessDeniedException
import com.iman.bnpl.application.service.sms.SmsService
import com.iman.bnpl.domain.user.data.model.OtpTokenEntity
import com.iman.bnpl.domain.user.data.model.OtpType
import com.iman.bnpl.domain.user.data.repository.OtpTokenRepository
import org.springframework.stereotype.Service

@Service
class OtpTokenService(
    private val otpTokenRepository: OtpTokenRepository,
    private val smsService: SmsService
) {
    fun sendOtp(userId: String, phoneNumber: String) {
        val otpToken = otpTokenRepository.save(
            OtpTokenEntity(
                userId = userId,
                OtpType.LOGIN
            )
        )
        smsService.sendVerificationCodeSms(otpToken.token, phoneNumber)
    }
    
    fun validateOtpTokenForLogin(userId: String, otp: String) {
        otpTokenRepository.findByUserId(userId).let {
            if (!it.isPresent || otp != it.get().token) {
                throw AccessDeniedException("Invalid credentials")
            }
        }
    }
}