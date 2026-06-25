package com.senac.gerenciamentoviagens.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.senac.gerenciamentoviagens.data.AppDatabase
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.repository.GeminiRepository
import com.senac.gerenciamentoviagens.ui.screens.*
import com.senac.gerenciamentoviagens.ui.viewmodels.*
import kotlinx.serialization.Serializable

/**
 * Definição das rotas da aplicação utilizando Navigation 3.
 * Cada objeto/classe representa uma tela e pode carregar argumentos tipados.
 */
@Serializable
sealed interface Screen : NavKey {
    @Serializable data object Login : Screen
    @Serializable data object Register : Screen
    @Serializable data object ForgotPassword : Screen
    @Serializable data class Menu(val email: String) : Screen
    @Serializable data class Photos(val tripId: Int) : Screen
    @Serializable data class Itinerary(val tripId: Int) : Screen
}

/**
 * Grafo de navegação principal.
 * Resolve as chaves de tela (Screen) para seus respectivos componentes UI.
 */
@Composable
fun NavGraph(
    navigationState: NavigationState,
    navigator: Navigator,
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel,
    tripViewModel: TripViewModel,
    database: AppDatabase
) {
    // entryProvider mapeia cada tipo de Screen para um Composable
    val entryProvider = remember {
        entryProvider<NavKey> {
            // Definição da tela de Login
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
            // Definição da tela de Registro de Usuário
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
            // Definição da tela de Recuperação de Senha
            entry<Screen.ForgotPassword> {
                ForgotPasswordScreen(
                    onResetSent = {
                        navigator.goBack()
                    }
                )
            }
            // Definição do Menu Principal (passando o e-mail do usuário logado)
            entry<Screen.Menu> { key ->
                val screen = key as Screen.Menu
                tripViewModel.setUserIdByEmail(screen.email)
                MenuScreen(
                    email = screen.email,
                    tripViewModel = tripViewModel,
                    onNavigateToPhotos = { tripId ->
                        navigator.navigate(Screen.Photos(tripId))
                    },
                    onNavigateToItinerary = { tripId ->
                        navigator.navigate(Screen.Itinerary(tripId))
                    }
                )
            }
            // Definição da Galeria de Fotos da Viagem
            entry<Screen.Photos> { key ->
                val screen = key as Screen.Photos
                val photosViewModel: PhotosViewModel = viewModel(
                    factory = PhotosViewModelFactory(database.photoDao())
                )
                PhotosScreen(
                    tripId = screen.tripId,
                    viewModel = photosViewModel,
                    onBack = { navigator.goBack() }
                )
            }
            // Definição da Geração de Roteiro via IA (Gemini)
            entry<Screen.Itinerary> { key ->
                val screen = key as Screen.Itinerary
                val itineraryViewModel: ItineraryViewModel = viewModel(
                    factory = ItineraryViewModelFactory(GeminiRepository())
                )
                
                // Estado local para carregar os dados da viagem necessária para o prompt da IA
                var tripState by remember { mutableStateOf<Trip?>(null) }
                LaunchedEffect(screen.tripId) {
                    tripState = database.tripDao().getTripById(screen.tripId)
                }

                ItineraryScreen(
                    tripId = screen.tripId,
                    trip = tripState,
                    viewModel = itineraryViewModel,
                    onBack = { navigator.goBack() }
                )
            }
        }
    }

    // Componente do Navigation 3 que exibe a pilha de telas atual
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
