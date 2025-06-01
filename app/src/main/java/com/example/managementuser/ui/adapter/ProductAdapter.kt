package com.example.managementuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managementuser.R
import com.example.managementuser.data.product.ProductEntity

class ProductAdapter(
    private var products: List<ProductEntity>,
    private val onCheckedChange: (ProductEntity, Boolean) -> Unit
) : RecyclerView.Adapter<ProductAdapter.UserViewHolder>() {
    private val checkedStates = mutableMapOf<Int, Boolean>()

    fun setProducts(productList: List<ProductEntity>) {
        products = productList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productTitle = itemView.findViewById<TextView>(R.id.textTitle)
        private val productCategory = itemView.findViewById<TextView>(R.id.textCategory)
        private val productPrice = itemView.findViewById<TextView>(R.id.textPrice)
        private val checkbox = itemView.findViewById<CheckBox>(R.id.checkboxSelect)
        private val thumbnail = itemView.findViewById<ImageView>(R.id.imageThumbnail)

        fun bind(product: ProductEntity) {
            productTitle.text = product.title
            productCategory.text = product.category
            productPrice.text = String.format("$%.2f", product.price)
            Glide.with(itemView.context)
                .load(product.thumbnail)
                .into(thumbnail)

            checkbox.setOnCheckedChangeListener(null) // tránh callback lại khi reuse view
            checkbox.isChecked = checkedStates[product.id] == true

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                checkedStates[product.id] = isChecked
                onCheckedChange(product, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_products_view, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(products[position])
    }
}
