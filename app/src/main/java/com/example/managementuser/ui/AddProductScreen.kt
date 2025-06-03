package com.example.managementuser.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.managementuser.data.product.ProductEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var thumbnail by remember { mutableStateOf("") }
    var images by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val scroll = rememberScrollState()
    val context = LocalContext.current

    Scaffold { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFf0f4ff),
                            Color(0xFFe9f2fd),
                            Color(0xFFfafcff)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .padding(vertical = 30.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Add New Product",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 26.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            placeholder = { Text("Enter product name...") },
                            leadingIcon = { Icon(Icons.Filled.ShoppingCart, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            placeholder = { Text("Short description") },
                            leadingIcon = { Icon(Icons.Filled.Info, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("Price") },
                                placeholder = { Text("e.g. 199.99") },
                                leadingIcon = { Icon(Icons.Filled.ShoppingCart, null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(12.dp))
                            OutlinedTextField(
                                value = discount,
                                onValueChange = { discount = it },
                                label = { Text("Discount %") },
                                placeholder = { Text("e.g. 10") },
                                leadingIcon = { Icon(Icons.Filled.Clear, null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = rating,
                                onValueChange = { rating = it },
                                label = { Text("Rating") },
                                placeholder = { Text("e.g. 4.5") },
                                leadingIcon = { Icon(Icons.Filled.Star, null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(12.dp))
                            OutlinedTextField(
                                value = stock,
                                onValueChange = { stock = it },
                                label = { Text("Stock") },
                                placeholder = { Text("e.g. 20") },
                                leadingIcon = { Icon(Icons.Filled.LocationOn, null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Brand") },
                            placeholder = { Text("e.g. Apple") },
                            leadingIcon = { Icon(Icons.Filled.LocationOn, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            placeholder = { Text("e.g. Smartphone") },
                            leadingIcon = { Icon(Icons.Filled.LocationOn, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = thumbnail,
                            onValueChange = { thumbnail = it },
                            label = { Text("Thumbnail URL") },
                            placeholder = { Text("e.g. https://...") },
                            leadingIcon = { Icon(Icons.Filled.AddCircle, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = images,
                            onValueChange = { images = it },
                            label = { Text("Product Images (comma separated URLs)") },
                            placeholder = { Text("e.g. https://a.jpg, https://b.jpg") },
                            leadingIcon = { Icon(Icons.Filled.Face, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    try {
                                        // Validate input
                                        if (title.isBlank() || price.isBlank()) {
                                            errorMessage = "Title và Price không được để trống!"
                                            return@launch
                                        }
                                        // Parse các trường
                                        val priceVal = price.toDoubleOrNull()
                                        val discountVal = discount.toDoubleOrNull() ?: 0.0
                                        val ratingVal = rating.toDoubleOrNull() ?: 0.0
                                        val stockVal = stock.toIntOrNull() ?: 0
                                        if (priceVal == null) {
                                            errorMessage = "Giá sản phẩm không hợp lệ!"
                                            return@launch
                                        }
                                        // Parse images
                                        val imagesList = images.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                                        // Gọi API thêm sản phẩm
//                                        val request = ProductEntity(
//                                            title = title,
//                                            description = description,
//                                            price = priceVal,
//                                            discountPercentage = discountVal,
//                                            rating = ratingVal,
//                                            stock = stockVal,
//                                            brand = brand,
//                                            category = category,
//                                            thumbnail = thumbnail,
//                                            images = imagesList
//                                        )
//                                        val api = com.example.managementuser.api.ApiClient.createService(
//                                            com.example.managementuser.api.product.ProductService::class.java
//                                        )
//                                        val response = api.addProduct(request) // suspend fun
//
//                                        // Hiển thị thông báo thành công
//                                        Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show()
//                                        navController.popBackStack()
                                    }catch (e: Exception){
                                        errorMessage = "Thêm sản phẩm thất bại: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                                // Gọi API hoặc xử lý logic ở đây
                                Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                                isLoading = false
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text("Add Product", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}