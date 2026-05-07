package com.senac.gerenciamentoviagens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.senac.gerenciamentoviagens.data.AppDatabase
import com.senac.gerenciamentoviagens.ui.screens.ForgotPasswordScreen
import com.senac.gerenciamentoviagens.ui.screens.LoginScreen
import com.senac.gerenciamentoviagens.ui.screens.MenuScreen
import com.senac.gerenciamentoviagens.ui.screens.RegisterScreen
import com.senac.gerenciamentoviagens.ui.viewmodels.RegisterViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.RegisterViewModelFactory
import com.senac.gerenciamentoviagens.ui.viewmodels.TaskViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.LoginViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
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
    navigator: Navigator,
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel,
    tripViewModel: TripViewModel,
    database: AppDatabase
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
                    },
                    viewModel = loginViewModel
                )
            }
            entry<Screen.Register> {
                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterViewModelFactory(database.userDao())
                )
                RegisterScreen(
                    onRegisterSuccess = {
                        navigator.goBack()
                    },
                    viewModel = registerViewModel
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
                tripViewModel.setUserIdByEmail(key.email)
                MenuScreen(email = key.email, tripViewModel = tripViewModel)
            }
        }
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
