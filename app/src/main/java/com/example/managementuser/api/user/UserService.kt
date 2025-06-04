package com.example.managementuser.api.user

import com.example.managementuser.api.user.response.LoginResponse
import com.example.managementuser.api.user.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentAuthUser(@Header("Authorization") token: String): LoginResponse
}
