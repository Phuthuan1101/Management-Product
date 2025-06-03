package com.example.managementuser.ui.nav

/**
 * Định nghĩa các route màn hình dùng cho Navigation Compose.
 * Sử dụng sealed class để type-safe và dễ mở rộng.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Products : Screen("products")
    object AddProduct : Screen("addProduct")
    object Profile : Screen("profile")
    object ProductDetail : Screen("product-detail/{productJson}") {
        fun createRoute(productJson: String): String = "product-detail/$productJson"
    }
    object EditProduct : Screen("edit-product/{id}") {
        fun createRoute(id: Int): String = "edit-product/$id"
    }
}