package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.managementuser.R
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.request.LoginRequest
import com.example.managementuser.api.user.UserService
import com.example.managementuser.data.DataBaseApplication
import com.example.managementuser.data.user.UserDao
import com.example.managementuser.data.user.UserRepository
import com.example.managementuser.helper.PrefsHelper
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var db: DataBaseApplication
    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepository
    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_main)

        prefs = PrefsHelper(this@LoginActivity)
        db = DataBaseApplication.Companion.getInstance(this)
        userDao = db.userDao()
        repository = UserRepository(userDao, prefs)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        val progressBarLogin = findViewById<ProgressBar>(R.id.progressBar_login)
        lifecycleScope.launch {
            try {
                progressBarLogin.visibility = View.VISIBLE
                val api = lazy {
                    ApiClient.createService(
                        serviceClass = UserService::class.java
                    )
                }
                val response = api.value.login(LoginRequest(username, password, 60))

                repository.saveLoginInfo(response)
                Log.d("LoginActivity", "data login: $response")
                // TODO: Chuyển qua màn hình chính
                prefs.saveUser(response)
                //
                progressBarLogin.visibility = View.GONE
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Login failed: $username, $password")
                Log.e("LoginActivity", "Cause: ${e.message}")
                progressBarLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}