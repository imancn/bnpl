package com.iman.bnpl.actor.http.user.payload.response

data class RefreshTokenResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}