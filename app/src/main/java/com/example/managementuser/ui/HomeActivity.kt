package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managementuser.R
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.user.ProductService
import com.example.managementuser.data.DataBaseApplication
import com.example.managementuser.data.product.ProductRepository
import com.example.managementuser.helper.PrefsHelper
import com.example.managementuser.ui.fragment.ProductListFragment
import com.example.managementuser.ui.viewmodel.ProductListViewModel
import com.example.managementuser.ui.viewmodel.ProductListViewModelFactory
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var viewModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity) // LAYOUT CHỈ CHỨA NỘI DUNG

        if (!prefs.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupViewModel()
        setupLoadMoreButton()
        showHomeFragment() // Đúng chuẩn: chỉ HomeActivity mới gọi loadFragment
    }

    private fun setupViewModel() {
        val dao = DataBaseApplication.getInstance(this).productDao()
        val repository = ProductRepository(ApiClient.createService(ProductService::class.java), dao)
        val factory = ProductListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductListViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        lifecycleScope.launch {
            viewModel.loadMore()
        }
    }

    private fun setupLoadMoreButton() {
        findViewById<Button>(R.id.btnLoadMore).setOnClickListener {
            lifecycleScope.launch {
                viewModel.loadMore()
            }
        }
    }

    fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductListFragment())
            .commit()
    }
}