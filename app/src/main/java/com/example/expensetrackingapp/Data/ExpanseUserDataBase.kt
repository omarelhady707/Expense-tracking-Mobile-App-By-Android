package com.example.expensetrackingapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, ExpenseEntity::class], version = 1)
abstract class ExpanseUserDataBase : RoomDatabase(){
    abstract fun getUserDao() : UserDAO
    abstract fun getExpenseDao() : ExpenseDAO
    companion object {
        @Volatile
        private var INSTANCE: ExpanseUserDataBase? = null
        fun getDatabase(context: Context): ExpanseUserDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpanseUserDataBase::class.java,
                    "expense_tracker_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}