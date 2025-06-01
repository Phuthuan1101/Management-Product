package com.example.managementuser.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.managementuser.R
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.request.ProductRequest
import com.example.managementuser.api.user.ProductService
import kotlinx.coroutines.launch

class AddProductActivity : BaseActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtDiscount: EditText
    private lateinit var edtRating: EditText
    private lateinit var edtStock: EditText
    private lateinit var edtBrand: EditText
    private lateinit var edtCategory: EditText
    private lateinit var edtThumbnail: EditText
    private lateinit var edtImages: EditText
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        edtPrice = findViewById(R.id.edtPrice)
        edtDiscount = findViewById(R.id.edtDiscount)
        edtRating = findViewById(R.id.edtRating)
        edtStock = findViewById(R.id.edtStock)
        edtBrand = findViewById(R.id.edtBrand)
        edtCategory = findViewById(R.id.edtCategory)
        edtThumbnail = findViewById(R.id.edtThumbnail)
        edtImages = findViewById(R.id.edtImages)
        btnAdd = findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener { addProduct() }
    }

    private fun addProduct() {
        val title = edtTitle.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val price = edtPrice.text.toString().toIntOrNull() ?: 0
        val discount = edtDiscount.text.toString().toDoubleOrNull() ?: 0.0
        val rating = edtRating.text.toString().toDoubleOrNull() ?: 0.0
        val stock = edtStock.text.toString().toIntOrNull() ?: 0
        val brand = edtBrand.text.toString().trim()
        val category = edtCategory.text.toString().trim()
        val thumbnail = edtThumbnail.text.toString().trim()
        val images = edtImages.text.toString().split(',').map { it.trim() }.filter { it.isNotEmpty() }

        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ProductRequest(
            title = title,
            description = description,
            price = price,
            discountPercentage = discount,
            rating = rating,
            stock = stock,
            brand = brand,
            category = category,
            thumbnail = thumbnail,
            images = images
        )

        showLoading(true)
        lifecycleScope.launch {
            try {
                val productService = ApiClient.createService(ProductService::class.java)
                val response = productService.addProduct(request)
                showLoading(false)
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductActivity, "Product added!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddProductActivity, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@AddProductActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}