package com.example.budgetplanner.ViewModell

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainDashBoardViewModel(private val db : ExpenseUserDataBase,   private val userId: Int): ViewModel() {
   val userinfo = mutableStateOf<UserEntity?>(null)
    val userexpanses = mutableStateOf<List<ExpenseEntity>?>(null)

    init{
        load_userinfo()
        load_userExpances()
    }
    fun load_userExpances() {
        viewModelScope.launch(Dispatchers.IO) {
            val user_expanses = db.getExpenseDao().get_all_Expense_aboutUser(userId)
            userexpanses.value = user_expanses
        }
    }
    fun load_userinfo(){
    viewModelScope.launch(Dispatchers.IO) {
        val user = db.getUserDao().getUserById(userId)
        userinfo.value = user
    }

}

}