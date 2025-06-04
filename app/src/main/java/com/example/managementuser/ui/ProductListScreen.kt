package com.example.managementuser.ui

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.managementuser.MyApp
import com.example.managementuser.data.product.Dimensions
import com.example.managementuser.data.product.Meta
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.data.product.ProductRepository
import com.example.managementuser.ui.nav.Screen
import com.example.managementuser.ui.viewmodel.ProductListViewModel
import com.example.managementuser.ui.viewmodel.ProductListViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(
    context: Context,
    navHostController: NavHostController,
    onDelete: (Int) -> Unit,
    onEdit: (ProductEntity) -> Unit
) {
    val repository: ProductRepository = MyApp.productRepository
    val viewModel: ProductListViewModel =
        viewModel(factory = ProductListViewModelFactory(repository))
    val products by viewModel.pagedProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Tự động load more khi đến cuối danh sách
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                if (lastVisible == products.lastIndex && !isLoading) {
                    Log.d("ProductListScreen", "load more product")
                    viewModel.loadMore()
                }
            }
    }

    Scaffold(
        modifier = Modifier.safeContentPadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                state = listState
            ) {
                items(products) { product ->
                    ProductItem(context, product, onDelete, onEdit, navHostController)
                }
                if (isLoading) {
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            if (products.isEmpty() && isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

            // FAB scroll to top
            if (products.isNotEmpty() && listState.firstVisibleItemIndex > 0) {
                FloatingActionButton (
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Back to top",
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    context: Context,
    product: ProductEntity,
    onDelete: (Int) -> Unit,
    onEdit: (ProductEntity) -> Unit,
    navHostController: NavHostController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = {
            val productJson = Uri.encode(Gson().toJson(product))
            navHostController.navigate(Screen.ProductDetail.createRoute(productJson))
        }
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    product.title.takeIf { it.isNotBlank() } ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                if (product.description.isNotBlank()) {
                    Text(
                        product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    "Category: ${product.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.primary
                )
                Text(
                    "Brand: ${product.brand?.ifBlank { "No brand" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.secondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Price: $${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.primary
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Stock: ${product.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (product.stock > 0) colorScheme.primary else colorScheme.error
                    )
                }
                Text(
                    "Rating: ${product.rating} ⭐",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.tertiary
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = colorScheme.error
                    )
                }
                IconButton(
                    onClick = { onEdit(product) }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = colorScheme.primary
                    )
                }
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(product.id)
                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                        navHostController.popBackStack()
                    }
                ) { Text("Delete", color = colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            },
            title = { Text("Confirm Delete") },
            text = { Text("Do you really want to delete this product?") }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ProductItemPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ProductItem(
            LocalContext.current,
            ProductEntity(
                id = 1,
                title = "",
                description = "",
                category = "",
                price = 9.7,
                discountPercentage = 6.0,
                rating = 7.6,
                stock = 2,
                tags = listOf(),
                brand = "",
                sku = "",
                weight = 12,
                dimensions = Dimensions(5.0, 5.0, 5.0),
                warrantyInformation = "",
                shippingInformation = "",
                availabilityStatus = "",
                reviews = listOf(),
                returnPolicy = "",
                minimumOrderQuantity = 12,
                meta = Meta("AA", "AAA", "AA", ""),
                images = listOf(),
                thumbnail = ""
            ),
            {},
            {},
            rememberNavController()
        )
    }
}