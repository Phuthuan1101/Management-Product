package com.example.managementuser.helper

import android.content.Context
import android.content.SharedPreferences
import com.example.managementuser.api.response.LoginResponse
import com.example.managementuser.data.user.UserEntity
import com.google.gson.Gson

class PrefsHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    // --- Save user login session ---
    fun saveUser(user: LoginResponse) {
        val json = gson.toJson(user)
        prefs.edit().putString("logged_in_user", json).apply()
    }

    fun getUser(): LoginResponse? {
        val json = prefs.getString("logged_in_user", null)
        return json?.let {
            gson.fromJson(it, LoginResponse::class.java)
        }
    }

    fun getAccessToken(): String? {
        return getUser()?.accessToken
    }

    // --- Logout ---
    fun clearUserSession() {
        prefs.edit().remove("logged_in_user").apply()
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }
}
