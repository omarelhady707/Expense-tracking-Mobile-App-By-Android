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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MainDashboardScreen(navController: NavHostController) {

    val recentExpenses = listOf<String>()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // ====== Greeting ======
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.size(45.dp)
                    )
                }

                Column {
                    Text(
                        text = "Amr Muhammed",
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

            // ====== Balance Card ======
            BalanceCard()

            Spacer(modifier = Modifier.height(24.dp))

            // ====== Recent Expenses ======
            Text(
                text = "Recent Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (recentExpenses.isEmpty()) {
                Text(
                    text = "No expenses yet.",
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(recentExpenses) { item ->
                        Text(text = item)
                        Spacer(modifier = Modifier.height(8.dp))
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
fun BalanceCard() {
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
                text = "$2,548.00",
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
                        "Income",
                        color = Color.White.copy(0.7f)
                    )
                    Text(
                        "$10,840.00",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        "Expenses",
                        color = Color.White.copy(0.7f)
                    )
                    Text(
                        "$1,884.00",
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

