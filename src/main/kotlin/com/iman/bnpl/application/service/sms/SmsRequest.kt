package com.iman.bnpl.application.service.sms

data class SmsRequest(
    val code: String,
    val sender: String,
    val recipient: String,
    val variable: Map<String, String>
)