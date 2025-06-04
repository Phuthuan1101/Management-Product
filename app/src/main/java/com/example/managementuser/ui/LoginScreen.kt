package com.example.managementuser.ui

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.managementuser.api.user.response.LoginResponse
import com.example.managementuser.data.product.Dimensions
import com.example.managementuser.data.product.Meta
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.ui.nav.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    context: Context,
    navController: NavController,
    onLogin: suspend (String, String) -> Unit,
    productsBestSale: List<ProductEntity>
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Row: Avatar label, notification icon + badge
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Avatar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Box {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = colorScheme.onBackground
                        )
                    }
                    // Notification badge
                    Box(
                        Modifier
                            .size(16.dp)
                            .offset(x = 18.dp, y = 4.dp)
                            .background(Color.Red, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "2",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    colorScheme.primaryContainer,
                                    colorScheme.secondaryContainer
                                )
                            )
                        )
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = colorScheme.primary
                    )
                }
            }
            Text("Login", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(32.dp))
            // Email
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                leadingIcon = { Icon(Icons.Filled.Person, null, tint = Color.Black) },
                placeholder = { Text("Email or Username", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorTextColor = Color.Black,
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    cursorColor = colorScheme.primary,
                    focusedLeadingIconColor = Color.Black,
                    unfocusedLeadingIconColor = Color.Black,
                    disabledLeadingIconColor = Color.Black,
                    errorLeadingIconColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black,
                    disabledPlaceholderColor = Color.Black,
                    errorPlaceholderColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            Spacer(Modifier.height(12.dp))

            // Password
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = Color.Black)
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Lock
                    else Icons.Filled.Face

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                placeholder = { Text("Password", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorTextColor = Color.Black,
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    cursorColor = colorScheme.primary,
                    focusedLeadingIconColor = Color.Black,
                    unfocusedLeadingIconColor = Color.Black,
                    disabledLeadingIconColor = Color.Black,
                    errorLeadingIconColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black,
                    disabledPlaceholderColor = Color.Black,
                    errorPlaceholderColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
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
                                onLogin(username, password)
                                navController.navigate(Screen.Products.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
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
                }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Đang đăng nhập...")
                } else {
                    Text("Đăng nhập")
                }
            }
            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BestSaleBanner(products = productsBestSale)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreview() {
    val bestSaleProducts = listOf(
        ProductEntity(
            id = 1,
            title = "iPhone 15 Pro Max",
            description = "Flagship Apple",
            category = "Smartphone",
            price = 1999.0,
            discountPercentage = 10.0,
            rating = 4.8,
            stock = 12,
            tags = listOf(),
            brand = "Apple",
            sku = "IP15PM2025",
            weight = 220,
            dimensions = Dimensions(5.0, 3.0, 0.7),
            warrantyInformation = "12 tháng",
            shippingInformation = "Free ship",
            availabilityStatus = "Còn hàng",
            reviews = listOf(),
            returnPolicy = "Đổi trả trong 7 ngày",
            minimumOrderQuantity = 1,
            meta = Meta("A", "B", "C", "D"),
            images = listOf(),
            thumbnail = "https://dummyimage.com/600x400/000/fff&text=Pro+Max"
        ),
        ProductEntity(
            id = 2,
            title = "Samsung S24 Ultra",
            description = "Sang trọng, camera zoom đỉnh",
            category = "Smartphone",
            price = 1799.0,
            discountPercentage = 12.0,
            rating = 4.7,
            stock = 9,
            tags = listOf(),
            brand = "Samsung",
            sku = "SS24U2025",
            weight = 218,
            dimensions = Dimensions(5.1, 3.1, 0.8),
            warrantyInformation = "12 tháng",
            shippingInformation = "Miễn phí",
            availabilityStatus = "Còn hàng",
            reviews = listOf(),
            returnPolicy = "Đổi trả 15 ngày",
            minimumOrderQuantity = 1,
            meta = Meta("E", "F", "G", "H"),
            images = listOf(),
            thumbnail = "https://dummyimage.com/600x400/111/fff&text=S24+Ultra"
        )
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        LoginScreen(
            LocalContext.current,
            rememberNavController(),
            { _, _ ->
                // Trả về dữ liệu giả lập
                LoginResponse(
                    id = 0,
                    username = "",
                    firstName = "",
                    lastName = "",
                    gender = "",
                    image = "",
                    accessToken = "",
                    refreshToken = "",
                    email = ""
                )
            },
            productsBestSale = bestSaleProducts
        )
    }
}

