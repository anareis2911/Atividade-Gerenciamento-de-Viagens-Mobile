package com.senac.gerenciamentoviagens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.senac.gerenciamentoviagens.ui.screens.ForgotPasswordScreen
import com.senac.gerenciamentoviagens.ui.screens.LoginScreen
import com.senac.gerenciamentoviagens.ui.screens.MenuScreen
import com.senac.gerenciamentoviagens.ui.screens.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable data object Login : Screen
    @Serializable data object Register : Screen
    @Serializable data object ForgotPassword : Screen
    @Serializable data class Menu(val email: String) : Screen
}

@Composable
fun NavGraph(
    navigationState: NavigationState,
    navigator: Navigator
) {
    val entryProvider = remember {
        entryProvider<NavKey> {
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = { email ->
                        navigator.navigate(Screen.Menu(email))
                    },
                    onNavigateToRegister = {
                        navigator.navigate(Screen.Register)
                    },
                    onNavigateToForgotPassword = {
                        navigator.navigate(Screen.ForgotPassword)
                    }
                )
            }
            entry<Screen.Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        navigator.goBack()
                    }
                )
            }
            entry<Screen.ForgotPassword> {
                ForgotPasswordScreen(
                    onResetSent = {
                        navigator.goBack()
                    }
                )
            }
            entry<Screen.Menu> { key ->
                MenuScreen(email = key.email)
            }
        }
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
