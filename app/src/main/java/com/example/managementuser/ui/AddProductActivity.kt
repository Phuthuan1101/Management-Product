package com.example.managementuser.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managementuser.R

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val titleInput = findViewById<EditText>(R.id.inputTitle)
        val categoryInput = findViewById<EditText>(R.id.inputCategory)
        val priceInput = findViewById<EditText>(R.id.inputPrice)
        val addButton = findViewById<Button>(R.id.btnAddProduct)

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val category = categoryInput.text.toString()
            val price = priceInput.text.toString().toDoubleOrNull()

            if (title.isBlank() || category.isBlank() || price == null) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Thêm vào DB hoặc gọi API lưu sản phẩm
            Toast.makeText(this, "Đã thêm sản phẩm: $title", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
} 
