package com.example.managementuser.api.user.request

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 60
)
