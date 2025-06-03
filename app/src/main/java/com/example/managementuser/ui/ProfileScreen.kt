package com.example.managementuser.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

// Mô phỏng response từ https://dummyjson.com/auth/me
data class UserProfile(
    val id: Int = 1,
    val firstName: String = "John",
    val lastName: String = "Doe",
    val email: String = "john.doe@example.com",
    val username: String = "johndoe",
    val gender: String = "male",
    val image: String = "https://robohash.org/johndoe.png"
)

@Composable
fun ProfileScreen(navController: NavHostController) {
    // Ở thực tế, bạn sẽ fetch từ ViewModel hoặc Repository bằng coroutine
    var user by remember {
        mutableStateOf(
            UserProfile(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                username = "johndoe",
                gender = "male",
                image = "https://dummyjson.com/icon/emilys/128"
            )
        )
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar và nút sửa
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.size(110.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    )
                    IconButton(
                        onClick = { Toast.makeText(context, "Edit avatar", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier
                            .offset(x = (-8).dp, y = (-8).dp)
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 24.sp
                )
                Text(
                    "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Card(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F5FC))
                ) {
                    Column(Modifier.padding(18.dp)) {
                        ProfileField(label = "Email", value = user.email)
                        ProfileField(label = "Gender", value = user.gender.replaceFirstChar { it.uppercase() })
                        ProfileField(label = "User ID", value = user.id.toString())
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        Toast.makeText(context, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                        // Xử lý logout, ví dụ: navController.navigate("login") { ... }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đăng xuất", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(Modifier.padding(bottom = 10.dp)) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Gray, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Normal, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PrvProfileScreen() {
    ProfileScreen(rememberNavController())
}