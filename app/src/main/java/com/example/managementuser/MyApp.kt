package com.example.managementuser

import android.app.Application
import android.util.Log
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.product.ProductService
import com.example.managementuser.api.user.UserService
import com.example.managementuser.data.DataBaseApplication
import com.example.managementuser.data.product.ProductRepository
import com.example.managementuser.data.user.UserRepository
import com.example.managementuser.helper.PrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MyApp : Application() {

    companion object {
        lateinit var productService: ProductService
        lateinit var productRepository: ProductRepository
        lateinit var userService: UserService
        lateinit var userRepository: UserRepository
        lateinit var prefsHelper: PrefsHelper
    }

    override fun onCreate() {
        super.onCreate()
        prefsHelper = PrefsHelper(applicationContext)
        val dao = DataBaseApplication.getInstance(applicationContext)
        productService = ApiClient.createService(ProductService::class.java)
        productRepository = ProductRepository(api = productService, dao = dao.productDao())
        userService = ApiClient.createService(UserService::class.java)
        userRepository = UserRepository(prefsHelper = prefsHelper, userDao = dao.userDao())
//        runBlocking {
//            productRepository.refreshProducts()
//        }
        runBlocking {
            withContext(Dispatchers.IO) {
                Log.d("List user current", "Data : ${userRepository.getAllUse()}")
            }
        }
    }
}