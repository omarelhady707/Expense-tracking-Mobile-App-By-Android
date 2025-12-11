package com.example.budgetplanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetplanner.UIpages.*
import com.example.budgetplanner.ui.theme.BudgetPlannerTheme
import androidx.compose.foundation.layout.padding
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()




        setContent {
            BudgetPlannerTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun AppNavigation(navController: NavHostController) {
    var currentUserId by remember { mutableStateOf<Int?>(null) }
    NavHost(navController = navController, startDestination = "login") {

        // ===== Login Screen =====
        composable("login") {
            LoginScreen(
                onLoginClick = { id ->
                    currentUserId = id
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        // ===== SignUp Screen =====
        composable("signup") {
            SignUpScreen(
                onSignUpClick = { id ->
                    currentUserId = id
                    navController.navigate("dashboard") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackToLoginClick = { navController.popBackStack() }
            )
        }

        // ===== Dashboard Screen WITH BottomNavigation =====
        composable("dashboard") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                currentUserId?.let { userId ->
                    MainDashboardScreen(
                        modifier = Modifier.padding(innerPadding),
                        navController,
                        userId
                    )
                }
            }
        }
        // ===== AddExpense Screen WITH BottomNavigation =====
        composable("addExpense") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                AddExpenseScreen(modifier = Modifier.padding(innerPadding), currentUserId)
            }
        }

        // ===== Account Screen WITH BottomNavigation =====
        composable("account") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                currentUserId?.let { userId ->
                    AccountScreen(modifier = Modifier.padding(innerPadding), userId, onEditClick = {
                        navController.navigate("editAccount")
                    }
                    )
                }
            }
        }
        composable("editAccount") {
            currentUserId?.let { userId ->
                EditAccountScreen(
                    user_Id = userId,
                    navController = navController
                )
            }
        }

    }
}
