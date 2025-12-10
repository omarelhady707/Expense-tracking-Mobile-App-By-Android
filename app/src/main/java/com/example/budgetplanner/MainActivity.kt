package com.example.budgetplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun AppNavigation(navController: NavHostController) {
    var currentUserId by remember { mutableStateOf<Int?>(null) } // لتخزين الـ userId بعد تسجيل الدخول

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
                onSignUpClick = { name, email, password ->
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
                    MainDashboardScreen(navController, userId)
                }
            }

            // ===== AddExpense Screen WITH BottomNavigation =====
            composable("addExpense") {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    AddExpenseScreen()
                }
            }

            // ===== Account Screen WITH BottomNavigation =====
            composable("account") {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    AccountScreen()
                }
            }
        }
    }
}
