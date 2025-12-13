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
    val BalancePrice = mutableStateOf<Double?>(0.0)

    init {
        load_userinfo()
        load_userExpances()
    }

    fun CalcBalancePrice() {
        val user = userinfo.value
        val expenses = userexpanses.value

        if (user != null && expenses != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val totalExpenses: Double = expenses.sumOf { it.amount }
                db.getUserDao().updateTotExpense(userId, totalExpenses)
                BalancePrice.value = user.budget - totalExpenses
            }
        }
    }

    fun load_userExpances() {
        viewModelScope.launch(Dispatchers.IO) {
            val user_expanses = db.getExpenseDao().get_all_Expense_aboutUser(userId)
            userexpanses.value = user_expanses

            CalcBalancePrice()
        }
    }

    fun load_userinfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = db.getUserDao().getUserById(userId)
            userinfo.value = user
        }
    }
    fun deleteExpenseById(expenseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().deleteExpensePerId(expenseId)
        }
    }
}