package com.iman.bnpl.domain.user.data.model

enum class OtpType(val expiryTimeSec: Int) {
    LOGIN(120),
    RESET_PASSWORD(600)
}
