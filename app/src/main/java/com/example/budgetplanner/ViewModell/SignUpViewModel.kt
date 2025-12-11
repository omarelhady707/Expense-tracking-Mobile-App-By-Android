package com.example.budgetplanner.ViewModell

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(private val db: ExpenseUserDataBase) : ViewModel(){
    var user_id= mutableStateOf<Int?>(null)
    fun  AddNewUserandGetId(newuser: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
          val id = db.getUserDao().addUser(newuser)
            user_id.value = id.toInt()
        }
    }
}