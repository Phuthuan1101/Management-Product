package com.example.managementuser.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.managementuser.R
import com.example.managementuser.data.DataBaseApplication
import kotlinx.coroutines.launch

class EditProductActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        titleEditText = findViewById(R.id.editTitle)
        descEditText = findViewById(R.id.editDescription)
        priceEditText = findViewById(R.id.editPrice)
        saveButton = findViewById(R.id.btnSave)

        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            val dao = DataBaseApplication.getInstance(this).productDao()
            lifecycleScope.launch {
                val product = dao.getProductById(productId)
                product?.let { productEdit ->
                    {
                        titleEditText.setText(productEdit.title)
                        descEditText.setText(productEdit.description)
                        priceEditText.setText(productEdit.price.toString())

                        saveButton.setOnClickListener {
                            val updated = productEdit.copy(
                                title = titleEditText.text.toString(),
                                description = descEditText.text.toString(),
                                price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
                            )
                            lifecycleScope.launch {
                                dao.updateProduct(updated)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}