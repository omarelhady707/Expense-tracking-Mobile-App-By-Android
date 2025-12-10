package com.example.budgetplanner.ViewModell

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val db: ExpenseUserDataBase): ViewModel() {
    var loginResult = mutableStateOf(false)
    var user_id= mutableStateOf<Int?>(null)
    fun getUserByEmailandPassword(email: String, password:String) {
        viewModelScope.launch(Dispatchers.IO) {
       val user = db.getUserDao().login(email,password)
            if(user!=null){
                user_id.value=user.userId
                loginResult.value=true
            }else{
                loginResult.value=false
            }
    }

}
}