package com.example.budgetplanner.ViewModell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel(private val db: ExpenseUserDataBase,private val user_Id: Int): ViewModel(){
    fun UpdateUserBudget(newbudget: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getUserDao().updateBudget(user_Id, newbudget)
        }
    }
    fun UpdateUserPassword(pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getUserDao().updatePassword(user_Id, pass)
        }
    }
}