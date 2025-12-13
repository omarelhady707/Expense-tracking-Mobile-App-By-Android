package com.example.budgetplanner.ViewModell

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainDashBoardViewModel(
    private val db: ExpenseUserDataBase,
    private val userId: Int
) : ViewModel() {

    val userinfo = mutableStateOf<UserEntity?>(null)
    val userexpanses = mutableStateOf<List<ExpenseEntity>>(emptyList())
    val BalancePrice = mutableStateOf(0.0)

    init {
        load_userinfo()
        load_userExpances()
    }

    fun load_userinfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = db.getUserDao().getUserById(userId)
            userinfo.value = user
        }
    }

    fun load_userExpances() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenses = db.getExpenseDao().get_all_Expense_aboutUser(userId)

            withContext(Dispatchers.Main) {
                userexpanses.value = expenses
            }

            CalcBalancePrice()
        }
    }

  /*  fun CalcBalancePrice() {
        val user = userinfo.value
        val expenses = userexpanses.value

        if (user != null) {
            val total = expenses.sumOf { it.amount }


            viewModelScope.launch(Dispatchers.IO) {
                db.getUserDao().updateTotExpense(userId, total)
            }


            viewModelScope.launch(Dispatchers.Main) {
                BalancePrice.value = user.budget - total
            }
        }
    }*/
    fun CalcBalancePrice() {
        val user = userinfo.value
        val expenses = userexpanses.value

        if (user != null) {
            val total = expenses.sumOf { it.amount }

            viewModelScope.launch(Dispatchers.IO) {

              
                db.getUserDao().updateTotExpense(userId, total)


                val updatedUser = db.getUserDao().getUserById(userId)


                withContext(Dispatchers.Main) {
                    userinfo.value = updatedUser
                    BalancePrice.value = updatedUser!!.budget - total
                }
            }
        }
    }

    fun deleteExpenseById(expenseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().deleteExpensePerId(expenseId)


            load_userExpances()
        }
    }
}