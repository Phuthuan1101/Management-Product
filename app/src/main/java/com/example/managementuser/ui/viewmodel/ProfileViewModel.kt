package com.example.managementuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.managementuser.data.user.UserRepository
import com.example.managementuser.data.user.UserEntity
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val currentUser = userRepository.getCurrentUser()

    fun updateUser(updatedUser: UserEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
            onComplete()
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onComplete()
        }
    }
}