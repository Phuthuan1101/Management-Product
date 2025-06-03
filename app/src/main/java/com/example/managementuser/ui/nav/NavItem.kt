package com.example.managementuser.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavItem("home", Icons.Filled.Home, "Home")
    object Products : NavItem("products", Icons.Filled.List, "Products")
    object AddProduct : NavItem("addProduct", Icons.Filled.Add, "Add")
    object Profile : NavItem("profile", Icons.Filled.Person, "Profile")
    
    companion object {
        val items = listOf(Home, Products, AddProduct, Profile)
    }
}