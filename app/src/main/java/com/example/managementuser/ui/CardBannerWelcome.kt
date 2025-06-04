package com.example.managementuser.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.managementuser.data.product.Dimensions
import com.example.managementuser.data.product.Meta
import com.example.managementuser.data.product.ProductEntity

@Composable
fun BestSaleBanner(products: List<ProductEntity>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        Text(
            text = "🔥 Best Sale Products",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(products) { product ->
                BestSaleProductCard(product)
            }
        }
    }
}

@Composable
private fun BestSaleProductCard(product: ProductEntity) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(220.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = product.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$${product.price}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
            if (product.discountPercentage > 0) {
                Text(
                    "-${product.discountPercentage}%",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewBestSaleBanner() {
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
    MaterialTheme(colorScheme = lightColorScheme()){
        BestSaleBanner(products = bestSaleProducts)
    }
}