package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.managementuser.R
import com.example.managementuser.helper.PrefsHelper
import com.example.managementuser.ui.fragment.ProductListFragment
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected lateinit var toolbar: Toolbar
    protected lateinit var loadingOverlay: View
    protected lateinit var prefs: PrefsHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
        prefs = PrefsHelper(this)
        setupBaseViews()

        setupNavigationDrawer()
        setupBackPressedHandler()
        setupHeaderUserInfo()
    }

    private fun setupBaseViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.toolbar)
        loadingOverlay = findViewById(R.id.loadingOverlay)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun setContentView(layoutResID: Int) {
        val container = findViewById<FrameLayout>(R.id.activity_container)
        layoutInflater.inflate(layoutResID, container, true)
    }

    // Hàm show/hide loading overlay dùng chung cho mọi activity
    fun showLoading(show: Boolean) {
        loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    // Có thể override hàm này nếu activity không cần navigation drawer
    open fun enableNavigationDrawer(enable: Boolean) {
        drawerLayout.setDrawerLockMode(
            if (enable) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        )
    }

    private fun setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        enableNavigationDrawer(true)
    }
    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupHeaderUserInfo() {
        val headerView = navigationView.getHeaderView(0)
        val imageAvatar = headerView.findViewById<android.widget.ImageView>(R.id.imageAvatar)
        val textUsername = headerView.findViewById<android.widget.TextView>(R.id.textUsername)
        val textEmail = headerView.findViewById<android.widget.TextView>(R.id.textEmail)
        val user = prefs.getUser()
        if (user != null) {
            textUsername.text = "${user.firstName} ${user.lastName}"
            textEmail.text = user.userName
            Glide.with(this)
                .load(user.image)
                .into(imageAvatar)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("NAV", "Clicked item: ${item.itemId}")
        when (item.itemId) {
            R.id.nav_home -> loadFragment(ProductListFragment())
            R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_products -> startActivity(Intent(this, AddProductActivity::class.java))
            R.id.nav_setting -> {/* TODO: Handle settings */}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}