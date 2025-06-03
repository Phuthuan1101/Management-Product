package com.example.managementuser.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text(
                    text = "Avatar",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text("User Profile", style = MaterialTheme.typography.titleLarge)
            // Hiển thị thông tin user ở đây
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrvProfileScreen() {
    ProfileScreen(rememberNavController())
}