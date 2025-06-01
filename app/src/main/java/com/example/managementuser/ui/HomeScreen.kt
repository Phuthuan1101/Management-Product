package com.example.managementuser.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("products") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Product List")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("addProduct") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("profile") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Profile")
            }
        }
    }
}