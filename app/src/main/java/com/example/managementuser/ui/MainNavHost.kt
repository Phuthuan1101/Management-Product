package com.example.managementuser.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.managementuser.ui.screens.ProductListScreen

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("products") { ProductListScreen() } // Compose tự lấy ViewModel
        composable("addProduct") { AddProductScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}