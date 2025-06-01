package com.example.managementuser.api.response

import com.example.managementuser.data.product.ProductEntity

data class ProductResponse(
    val products: List<ProductEntity>,
    val total: Int,
    val skip: Int,
    val limit: Int
)