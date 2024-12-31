package com.iman.bnpl.domain.user.services

import com.iman.bnpl.application.advice.AccessDeniedException
import com.iman.bnpl.application.service.sms.SmsService
import com.iman.bnpl.domain.user.data.model.OtpEntity
import com.iman.bnpl.domain.user.data.model.OtpType
import com.iman.bnpl.domain.user.data.repository.OtpRepository
import org.springframework.stereotype.Service

@Service
class OtpService(
    private val otpRepository: OtpRepository,
    private val smsService: SmsService
) {
    fun validateOtpTokenForLogin(userId: String, otp: String, otpType: OtpType): Boolean {
        otpRepository.findByUserId(userId).let {
            if (!it.isPresent || otp != it.get().token) {
                throw AccessDeniedException("Invalid credentials")
            }
        }
        return true
    }
    
    fun sendLoginOtp(userId: String, phoneNumber: String) {
        val otpToken = createOtp(userId, OtpType.LOGIN)
        smsService.sendVerificationCodeSms(otpToken.token, phoneNumber)
    }
    
    fun sendResetPasswordOtp(userId: String, phoneNumber: String) {
        val otpToken = createOtp(userId, OtpType.RESET_PASSWORD)
        smsService.sendResetPasswordCodeSms(otpToken.token, phoneNumber)
    }
    
    private fun createOtp(userId: String, otpType: OtpType): OtpEntity {
        otpRepository.deleteByUserIdAndType(userId, otpType)
        val otpToken = otpRepository.save(OtpEntity(userId, otpType))
        return otpToken
    }
}