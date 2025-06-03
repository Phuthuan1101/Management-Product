package com.example.managementuser.data.product

import android.util.Log
import com.example.managementuser.api.product.ProductService
import kotlinx.coroutines.runBlocking

class ProductRepository(
    private val api: ProductService,
    private val dao: ProductDao
) {
    suspend fun refreshProducts() {
        val response = api.getAllProducts()
        Log.d("ProductRepository", "Size result call api: ${response.products.size}")
        Log.d("ProductRepository", "Result call api: $response")
        val productEntities = response.products.map { product ->
            ProductEntity(
                id = product.id,
                title = product.title,
                description = product.description,
                category = product.category,
                price = product.price,
                discountPercentage = product.discountPercentage,
                rating = product.rating,
                stock = product.stock,
                tags = product.tags,
                brand = product.brand,
                sku = product.sku,
                weight = product.weight,
                dimensions = product.dimensions,
                warrantyInformation = product.warrantyInformation,
                shippingInformation = product.shippingInformation,
                availabilityStatus = product.availabilityStatus,
                reviews = product.reviews,
                returnPolicy = product.returnPolicy,
                minimumOrderQuantity = product.minimumOrderQuantity,
                meta = product.meta,
                images = product.images,
                thumbnail = product.thumbnail
            )
        }
        dao.insertAll(productEntities)
    }

//    fun getAllProductLive(): LiveData<List<ProductEntity>> = dao.getAllProducts()

//    suspend fun fetchAndSaveProducts(limit: Int, skip: Int) {
//        try {
//            val response = api.getPagedProducts(limit, skip)
//            dao.insertAll(response.products)
//        } catch (e: Exception) {
//            throw e // để ViewModel xử lý hiển thị lỗi
//        }
//    }


    fun getLocalPagedProducts(limit: Int, page: Int): List<ProductEntity> {
        Log.d("ProductRepository", "Get data with limit: $limit and page: $page")
        return runBlocking {
            api.getPagedProducts(limit, page).products
        }
    }

    suspend fun deleteProductById(id : Int){
        api.deleteById(id)
    }
}
