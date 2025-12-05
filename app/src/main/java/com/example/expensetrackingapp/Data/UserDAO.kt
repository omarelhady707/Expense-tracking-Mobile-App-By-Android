package com.example.expensetrackingapp.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("Select * From usertable")
    suspend fun getAllUsers():List<UserEntity> //For Admin
    @Insert
    suspend fun addUser(user: UserEntity)
    @Delete
    suspend fun DeleteUser(user: UserEntity)
}