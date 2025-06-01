package com.example.managementuser.api.response

data class LoginResponse(
    val id: Int,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val accessToken : String,
    val refreshToken : String
)
