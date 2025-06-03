package com.example.managementuser.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.managementuser.MyApp
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.user.UserService
import com.example.managementuser.api.user.request.LoginRequest
import com.example.managementuser.api.user.response.LoginResponse
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.ui.screens.ProductListScreen
import com.example.managementuser.ui.viewmodel.ProductListViewModel
import com.example.managementuser.ui.viewmodel.ProductListViewModelFactory
import com.google.gson.Gson

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: ProductListViewModel =
        viewModel(factory = ProductListViewModelFactory(MyApp.productRepository))
    val context = LocalContext.current
    NavHost(
        navController = navController, startDestination = "login", modifier = modifier
    ) {
        composable("login") {
            LoginScreen(navController, ::login)
        }
        composable("home") { HomeScreen(navController) }
        composable("products") {
            ProductListScreen(
                context,
                navController,
                onDelete = { productId -> viewModel.deleteProduct(productId) },
                onEdit = { product ->
                    // Điều hướng tới màn edit, sau khi edit xong thì load lại product
                    navController.navigate("edit-product/${product.id}")
                })
        }
        composable("addProduct") { AddProductScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("product-detail/{productJson}") { backStackEntry ->
            val productJson = Uri.decode(backStackEntry.arguments?.getString("productJson"))
            val product = Gson().fromJson(productJson, ProductEntity::class.java)
            ProductDetailScreen(
                navHostController = navController,
                productEntity = product,
                onDelete = { productId -> viewModel.deleteProduct(productId) },
                onEdit = { product ->
                    // Điều hướng tới màn edit, sau khi edit xong thì load lại product
                    navController.navigate("edit-product/${product.id}")
                })
        }
    }
}

suspend fun login(username: String, pass: String): LoginResponse {
    return ApiClient.createService(UserService::class.java)
        .login(LoginRequest(username = username, password = pass))
}