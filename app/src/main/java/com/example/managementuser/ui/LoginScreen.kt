package com.example.managementuser.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.managementuser.MyApp
import com.example.managementuser.api.user.response.LoginResponse
import kotlinx.coroutines.launch
import com.example.managementuser.R

@Composable
fun LoginScreen(
    navController: NavController,
    onLogin: suspend (String, String) -> LoginResponse
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "",
                Modifier.size(200.dp)
            )
            Text("Login", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val response = onLogin(username, password)
                                MyApp.prefsHelper.saveUser(response)
                                MyApp.userRepository.saveLoginInfo(response)
                                navController.navigate("products") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Login failed: ${e.message}"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        errorMessage = "Please enter username and password"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Đang đăng nhập...")
                } else {
                    Text("Đăng nhập")
                }
            }
            val pagerState = rememberPagerState(pageCount = { 3 })
            val coroutineScope = rememberCoroutineScope()
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) { page ->
                    CardWelcome()
                }
            }
            // Indicator Dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                            .background(
                                if (pagerState.currentPage == index) Color(0xFFF857A6) else Color.LightGray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrvScreenLogin() {
    LoginScreen(rememberNavController(), ::login)
}

fun loginText(user: String, pass: String): LoginResponse {
    return LoginResponse(
        id = TODO(),
        userName = TODO(),
        firstName = TODO(),
        lastName = TODO(),
        gender = TODO(),
        image = TODO(),
        accessToken = TODO(),
        refreshToken = TODO()
    )
}

