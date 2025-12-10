package com.example.budgetplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.ExpenseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(private val db: ExpenseUserDataBase) : ViewModel() {
    fun AddExpense(User_Expense: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
        db.getExpenseDao().addExpense(User_Expense)
        }
    }
    fun get_AllExpense_per_UserID(User_Expense: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().get_all_Expense_aboutUser(User_Expense.userId)
        }
    }
    fun get_AllExpense_per_Date(User_Expense: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().get_all_Expense_aboutUser_WithDate(User_Expense.userId,User_Expense.date)
        }
    }
    fun DeleteExpensePerDate(User_Expense: ExpenseEntity){
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().deleteExpensePerDate(User_Expense.date)
        }
    }
    fun DeleteExpensePerID(User_Expense: ExpenseEntity){
        viewModelScope.launch(Dispatchers.IO) {
            db.getExpenseDao().deleteExpensePerId(User_Expense.userId)
        }
    }

}
