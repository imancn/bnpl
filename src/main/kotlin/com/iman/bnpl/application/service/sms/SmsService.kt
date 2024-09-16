package com.iman.bnpl.application.service.sms

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SmsService(
    private val smsClient: SmsClient,
    @Value("\${sms.api.key}") private val apiKey: String
) {
    fun sendVerificationCodeSms(otpToken: String, phoneNumber: String) {
        val patternCode = "ynq310ktkh49pgh"
        val request = SmsRequest(
            code = patternCode,
            sender = "+983000505",
            recipient = "+98$phoneNumber",
            variable = mapOf("verification-code" to otpToken)
        )
        
        smsClient.sendSms(apiKey, request)
    }
}
