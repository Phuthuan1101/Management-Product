package com.example.managementuser.data.user

import com.example.managementuser.api.user.response.LoginResponse
import com.example.managementuser.helper.PrefsHelper

// repository/UserRepository.kt
class UserRepository(
    private val userDao: UserDao,
    private val prefsHelper: PrefsHelper
) {

    suspend fun saveLoginInfo(userInfo: LoginResponse) {
        val userEntity = UserEntity(
            id = userInfo.id,
            firstName = userInfo.firstName,
            lastName = userInfo.lastName,
            userName = userInfo.username,
            image = userInfo.image,
            gender = userInfo.gender,
            accessToken = userInfo.accessToken,
            refreshToken = userInfo.refreshToken
        )
        userDao.insertUser(userEntity)
    }
    fun getAccLoginByUser(userName: String) = userDao.getAccLoginByUser(userName = userName)


    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun logout() {
        userDao.deleteAllUsers()
        prefsHelper.clearUserSession()
    }

    fun getCurrentUser() = userDao.getLoggedInUser()

    fun getAllUse() = userDao.getAllUser()
}
