package com.example.managementuser.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.ui.viewmodel.ProductListViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel()
) {
    val products by viewModel.pagedProducts.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    Scaffold(
        topBar = {
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (products.isEmpty() && isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(product)
                    }
                    item {
                        if (isLoading) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Button(
                                onClick = { viewModel.loadMore() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Load More")
                            }
                        }
                    }
                }
                if (errorMessage != null) {
                    Text(
                        errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: ProductEntity) {
    Card(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = product.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    product.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Category: ${product.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "Brand: ${product.brand ?: "No brand"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "Price: $${product.price} | Stock: ${product.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Rating: ${product.rating} ⭐",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}