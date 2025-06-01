package com.example.managementuser.api.user

import com.example.managementuser.api.response.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getAllProducts(): ProductResponse

    @GET("products")
    suspend fun getPagedProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponse
}