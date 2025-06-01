package com.example.managementuser.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.navigation.NavController

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

    val scroll = rememberScrollState()

    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scroll)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Title") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Description") }, modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Discount %") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Rating") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = brand, onValueChange = { brand = it },
                label = { Text("Brand") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Category") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = thumbnail, onValueChange = { thumbnail = it },
                label = { Text("Thumbnail URL") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = images, onValueChange = { images = it },
                label = { Text("Product Images (comma separated URLs)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    isLoading = true
                    // Gọi API thêm mới product ở đây, ví dụ:
                    // launch coroutine, call your repository, etc.
                    // Khi xong:
                    isLoading = false
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Add Product")
            }
        }
    }
}