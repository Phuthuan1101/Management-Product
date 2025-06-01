package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.managementuser.R
import com.example.managementuser.helper.PrefsHelper
import com.example.managementuser.ui.fragment.ProductListFragment
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var prefs: PrefsHelper
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PrefsHelper(this)
        if (!prefs.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        setupNavigationDrawer()
        setupBackPressedHandler()
    }
    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val headerView = navigationView.getHeaderView(0)
        val imageAvatar = headerView.findViewById<ImageView>(R.id.imageAvatar)
        val textUsername = headerView.findViewById<TextView>(R.id.textUsername)
        val textEmail = headerView.findViewById<TextView>(R.id.textEmail)
        val user = prefs.getUser()
        if (user != null) {
            textUsername.text = "${user.firstName} ${user.lastName}"
            textEmail.text = user.userName
            if (!user.image.isNullOrEmpty()) {
                Glide.with(this)
                    .load(user.image)
                    .placeholder(R.drawable.ic_avatar_default)
                    .error(R.drawable.ic_avatar_default)
                    .into(imageAvatar)
            } else {
                imageAvatar.setImageResource(R.drawable.ic_avatar_default)
            }
        }
    }

    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Nếu muốn kết thúc activity khi Drawer đã đóng:
                    finish()
                }
            }
        })
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}