package com.iman.bnpl.actor.http.user.payload.response

data class JwtResponse(
    var accessToken: String,
    var refreshToken: String,
    var phoneNumber: String?,
) {
    var tokenType = "Bearer"
}