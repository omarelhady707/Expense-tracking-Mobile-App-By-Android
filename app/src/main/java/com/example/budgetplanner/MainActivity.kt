package com.example.budgetplanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

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

        // ===== Dashboard Screen =====
        composable("dashboard") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) {
                MainDashboardScreen(navController)
            }
        }

        // ===== Add Expense Screen =====
        composable("addExpense") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) {
                AddExpenseScreen()
            }
        }

        // ===== Account Screen =====
        composable("account") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) {
                AccountScreen(
                    onEditClick = {
                        navController.navigate("editAccount")
                    }
                )
            }
        }


        // ===== Edit Account Screen =====
        composable("editAccount") {
            EditAccountScreen { phone, address, income, password ->
                navController.navigateUp()

                // TODO:Save in Database ya 7adu
            }
        }
    }
}
