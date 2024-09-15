package com.iman.bnpl.application.service.sms

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "smsClient", url = "https://api2.ippanel.com")
interface SmsClient {
    
    @PostMapping("/api/v1/sms/pattern/normal/send")
    fun sendSms(
        @RequestHeader("apikey") apiKey: String,
        @RequestBody request: SmsRequest
    ): Any
}
