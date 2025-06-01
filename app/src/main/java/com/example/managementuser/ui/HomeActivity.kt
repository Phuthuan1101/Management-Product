package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var prefs: PrefsHelper
    private lateinit var viewModel: ProductListViewModel
    private lateinit var progressOverlay: View
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_activity)

        prefs = PrefsHelper(this)
        if (!prefs.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        progressOverlay = findViewById(R.id.loadingOverlay)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Khởi tạo Repository và ViewModelFactory
        val dao = DataBaseApplication.Companion.getInstance(this).productDao()
        val repository = ProductRepository(
            ApiClient.createService(ProductService::class.java), dao
        )
        val factory = ProductListViewModelFactory(repository)

        // Thiết lập Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Thiết lập Drawer Toggle (hamburger icon)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Lấy ViewModel
        viewModel = ViewModelProvider(this, factory)[ProductListViewModel::class.java]

        // Quan sát isLoading để hiển thị progress overlay
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        // Load dữ liệu ban đầu
        lifecycleScope.launch {
            viewModel.loadMore()
        }

        // Xử lý button "Show more"
        findViewById<Button>(R.id.btnLoadMore).setOnClickListener {
            lifecycleScope.launch {
                viewModel.loadMore()
            }
        }

        // Load fragment hiển thị danh sách sản phẩm
        loadFragment(ProductListFragment())
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun showLoading(show: Boolean) {
        progressOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("NAV", "Clicked item: ${item.itemId}")
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(ProductListFragment())
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.nav_products -> {
                startActivity(Intent(this, AddProductActivity::class.java))
            }
            R.id.nav_setting -> {
                // Nếu có màn settings
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}