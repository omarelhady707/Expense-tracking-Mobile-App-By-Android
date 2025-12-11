package com.example.budgetplanner.UIpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.budgetplanner.R
import com.example.budgetplanner.ViewModell.EditFactory
import com.example.budgetplanner.ViewModell.EditViewModel
import com.example.budgetplanner.ViewModell.MainDashBoardViewModel
import com.example.budgetplanner.ViewModell.MainDashboardViewModelFactory
import com.example.expensetrackingapp.Data.ExpenseUserDataBase

@Composable
fun EditAccountScreen(
    user_Id:Int,
    navController : NavHostController
) {
    var BudgetState = remember { mutableStateOf(0.0) }
    var passwordState = remember { mutableStateOf("") }

    val db = ExpenseUserDataBase.getDatabase(LocalContext.current)
    val MainDasshviewModel: MainDashBoardViewModel =
        viewModel(factory = MainDashboardViewModelFactory(db, user_Id))
    val editviewModel: EditViewModel =
        viewModel(factory = EditFactory(db, user_Id))

    // Load initial data
    LaunchedEffect(MainDasshviewModel.userinfo.value) {
        MainDasshviewModel.userinfo.value?.let { user ->
            BudgetState.value = user.budget
            passwordState.value = user.Password
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Edit Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Budget
        OutlinedTextField(
            value = BudgetState.value.toString(),
            onValueChange = {
                BudgetState.value = it.toDoubleOrNull() ?: 0.0
            },
            label = { Text("Income") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
            editviewModel.UpdateUserBudget(BudgetState.value)
            editviewModel.UpdateUserPassword(passwordState.value)
                navController.navigateUp()     // ← هنا بس!

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
