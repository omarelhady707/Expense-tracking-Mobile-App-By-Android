package com.example.budgetplanner.UIpages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetplanner.ExpenseViewModel
import com.example.budgetplanner.ViewModell.LoginViewModel
import com.example.budgetplanner.ViewModell.LoginViewModelFactory
import com.example.expensetrackingapp.Data.ExpenseUserDataBase

@Composable
fun LoginScreen(
    onLoginClick: (Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val db = ExpenseUserDataBase.getDatabase(LocalContext.current)
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(db))
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()).padding(bottom = 200.dp)
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Expenseset",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter your Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.getUserByEmailandPassword(email,password)
                if(viewModel.loginResult.value){
                    print("Sucsess Login")
                }else{
                    print("Fail to Login")
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Login", fontSize = 16.sp)
        }
        LaunchedEffect(viewModel.loginResult.value) {
            if (viewModel.loginResult.value) {
                viewModel.user_id.value?.let { id ->
                    onLoginClick(id)  // <-- هنا بنمرر userId
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ", fontSize = 14.sp)
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
