package com.example.managementuser.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM users LIMIT 1")
    fun getLoggedInUser(): UserEntity?

    @Query("SELECT * FROM users WHERE firstName = :userName")
    fun getAccLoginByUser(userName: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUser(): List<UserEntity>
}