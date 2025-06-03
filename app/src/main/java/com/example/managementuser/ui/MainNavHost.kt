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
import com.example.managementuser.ui.nav.BaseScaffoldWithNavbar
import com.example.managementuser.ui.nav.Screen
import com.example.managementuser.ui.screens.ProductListScreen
import com.example.managementuser.ui.viewmodel.ProductListViewModel
import com.example.managementuser.ui.viewmodel.ProductListViewModelFactory

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: ProductListViewModel =
        viewModel(factory = ProductListViewModelFactory(MyApp.productRepository))
    val productsBestSales = viewModel.productsBestSale
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // Không có Navbar cho Login
        composable(Screen.Login.route) {
            LoginScreen(
                context = context,
                navController = navController,
                onLogin = ::login,
                productsBestSale = productsBestSales
            )
        }

        // Các màn khác có Navbar
        composable(Screen.Home.route) {
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.Home.route
            ) {
                HomeScreen(navController)
            }
        }
        composable(Screen.Products.route) {
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.Products.route
            ) {
                ProductListScreen(
                    context,
                    navController,
                    onDelete = { productId -> viewModel.deleteProduct(productId) },
                    onEdit = { product ->
                        navController.navigate(Screen.EditProduct.createRoute(product.id))
                    }
                )
            }
        }
        composable(Screen.AddProduct.route) {
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.AddProduct.route
            ) {
                AddProductScreen(navController)
            }
        }
        composable(Screen.Profile.route) {
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.Profile.route
            ) {
                ProfileScreen(navController)
            }
        }
        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productJson = Uri.decode(backStackEntry.arguments?.getString("productJson"))
            val product = com.google.gson.Gson().fromJson(productJson, ProductEntity::class.java)
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.ProductDetail.route
            ) {
                ProductDetailScreen(
                    navHostController = navController,
                    productEntity = product,
                    onDelete = { productId -> viewModel.deleteProduct(productId) },
                    onEdit = { product ->
                        navController.navigate(Screen.EditProduct.createRoute(product.id))
                    }
                )
            }
        }
        composable(Screen.EditProduct.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            BaseScaffoldWithNavbar(
                navController = navController,
                currentRoute = Screen.EditProduct.route
            ) {
                // TODO: EditProductScreen(navController, id)
            }
        }
    }
}

suspend fun login(username: String, pass: String): LoginResponse {
    return ApiClient.createService(UserService::class.java)
        .login(LoginRequest(username = username, password = pass))
}