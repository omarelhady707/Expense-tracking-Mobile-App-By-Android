package com.example.expensetrackingapp.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("Select * From usertable")
    suspend fun getAllUsers():List<UserEntity> //For Admin

    @Query("SELECT * FROM usertable WHERE userId = :id LIMIT 1")
    fun getUserById(id: Int): UserEntity?
    @Insert
    suspend fun addUser(user: UserEntity)
    @Delete
    suspend fun DeleteUser(user: UserEntity)

    @Query("SELECT * FROM usertable WHERE email = :email AND Password = :password ")
    suspend fun login(email: String, password: String): UserEntity?

}