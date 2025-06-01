package com.example.managementuser.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("User Profile", style = MaterialTheme.typography.titleLarge)
            // Hiển thị thông tin user ở đây
        }
    }
}