package com.example.budgetplanner.UIpages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.budgetplanner.ViewModell.MainDashBoardViewModel
import com.example.budgetplanner.ViewModell.MainDashboardViewModelFactory
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase

@Composable
fun MainDashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userId: Int
) {
    val db = ExpenseUserDataBase.getDatabase(LocalContext.current)
    val viewModel: MainDashBoardViewModel = viewModel(factory = MainDashboardViewModelFactory(db, userId))
    val list = viewModel.userexpanses.value ?: emptyList()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ===== Greeting =====
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.Blue,
                            modifier = Modifier.size(45.dp)
                        )
                    }

                    Column {
                        Text(
                            text = viewModel.userinfo.value?.name ?: "Erorr",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                        Text(
                            text = ", Good Morning",
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ===== Balance Card =====
            item {
                BalanceCard(userId, viewModel)
            }

            // ===== Recent Expenses Header =====
            item {
                Text(
                    text = "Recent Expenses",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ===== Expenses List =====
            if (list.isEmpty()) {
                item {
                    Text(
                        text = "No expenses yet.",
                        color = Color.Gray
                    )
                }
            } else {
                items(list) { expense ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = expense.category)
                        Text(text = "$${expense.amount}")
                    }
                }
            }
        }
    }
}

// ========================================
// BALANCE CARD
// ========================================
@Composable
fun BalanceCard(userId: Int, viewModel: MainDashBoardViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3F51F5)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = "Available Balance",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )

            Text(
                text = viewModel.BalancePrice.value.toString(),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column {
                    Text(
                        "Budget",
                        color = Color.White.copy(0.7f)
                    )
                    Text(
                        viewModel.userinfo.value?.budget.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        "Total Expenses",
                        color = Color.White.copy(0.7f)
                    )
                    Text(
                        viewModel.userinfo.value?.totExpense.toString() ?: "Erorr",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ========================================
// BOTTOM NAVIGATION
// ========================================
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("addExpense") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            label = { Text("Add") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("account") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Account") }
        )
    }
}
