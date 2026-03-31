package com.senac.gerenciamentoviagens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.senac.gerenciamentoviagens.ui.screens.ForgotPasswordScreen
import com.senac.gerenciamentoviagens.ui.screens.LoginScreen
import com.senac.gerenciamentoviagens.ui.screens.MenuScreen
import com.senac.gerenciamentoviagens.ui.screens.RegisterScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Menu : Screen("menu/{email}") {
        fun createRoute(email: String) = "menu/$email"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { email -> 
                    navController.navigate(Screen.Menu.createRoute(email)) 
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() }
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onResetSent = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Menu.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            MenuScreen(email = email)
        }
    }
}
