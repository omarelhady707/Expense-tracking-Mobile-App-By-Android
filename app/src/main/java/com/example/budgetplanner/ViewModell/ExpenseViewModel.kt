package com.example.budgetplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.ExpenseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(private val db: ExpenseUserDataBase) : ViewModel() {
    fun AddExpense_And_Update_Budget(User_Expense: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
        db.getExpenseDao().addExpense(User_Expense)
         val user =db.getUserDao().getUserById(User_Expense.userId)
         user.let {
             val newBudget:Double = (it?.budget?.minus(User_Expense.amount) ?: 0) as Double
            db.getUserDao().updateBudget(User_Expense.userId,newBudget)

             var old_tot_Expense= user?.totExpense?:0.0
             val newTotExpense:Double = User_Expense.amount+ old_tot_Expense
             db.getUserDao().updateTotExpense(User_Expense.userId,newTotExpense)
         }


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
