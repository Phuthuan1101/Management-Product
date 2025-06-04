package com.example.managementuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementuser.MyApp
import com.example.managementuser.api.ApiClient
import com.example.managementuser.api.user.UserService
import com.example.managementuser.api.user.request.LoginRequest
import com.example.managementuser.api.user.response.LoginResponse
import com.example.managementuser.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(repository: UserRepository) : ViewModel() {
    val api = ApiClient.createService(UserService::class.java)
    private val _currentUser = MutableStateFlow<LoginResponse?>(null)
    val currentUser: StateFlow<LoginResponse?> = _currentUser
    fun login(username: String, pass: String) {
        viewModelScope.launch {
            val response = api.login(LoginRequest(username = username, password = pass))
            MyApp.prefsHelper.saveUser(response)
            MyApp.userRepository.saveLoginInfo(response)
            val result = api.getCurrentAuthUser(response.accessToken)
            _currentUser.value = result
        }
    }


}