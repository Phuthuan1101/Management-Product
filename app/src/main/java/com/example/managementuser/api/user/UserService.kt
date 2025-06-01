package com.example.managementuser.api.user

import com.example.managementuser.api.request.LoginRequest
import com.example.managementuser.api.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
