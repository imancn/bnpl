package com.iman.bnpl.domain.user.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "otp_tokens")
@TypeAlias("otp_tokens")
data class OtpTokenEntity(
    @Id
    var id: String?,
    var userId: String,
    var token: String,
    var type: OtpType,
    @Indexed(name = "expiry_date_ttl", expireAfterSeconds = 0)
    var expiryDate: Date,
) {
    constructor(userId: String, type: OtpType) : this(
        id = null,
        userId = userId,
        token = makeRandom6DigitsToken(),
        type = type,
        expiryDate = calculateExpiryDate(type)
    )

    companion object {
        private fun makeRandom6DigitsToken(): String {
            val digit = Random().nextInt(899_999) + 100_000
            return String.format("%06d", digit)
        }

        private fun calculateExpiryDate(type: OtpType): Date {
            val cal = Calendar.getInstance()
            cal.time = Date(cal.time.time)
            cal.add(Calendar.SECOND, type.expiryTimeSec)
            return Date(cal.time.time)
        }
    }
}