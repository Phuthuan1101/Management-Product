package com.example.managementuser.api.product

import androidx.room.Delete
import com.example.managementuser.api.product.request.ProductRequest
import com.example.managementuser.api.product.response.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getAllProducts(): ProductResponse

    @GET("products")
    suspend fun getPagedProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponse

    @POST("products/add")
    suspend fun addProduct(@Body productRequest: ProductRequest): Response<ProductResponse>

    @Delete
    suspend fun deleteById(@Query("id") id : Int)
}