package com.senac.gerenciamentoviagens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagens.data.AppDatabase
import com.senac.gerenciamentoviagens.navigation.NavGraph
import com.senac.gerenciamentoviagens.navigation.Navigator
import com.senac.gerenciamentoviagens.navigation.Screen
import com.senac.gerenciamentoviagens.navigation.rememberNavigationState
import com.senac.gerenciamentoviagens.ui.theme.GerenciamentoViagensTheme
import com.senac.gerenciamentoviagens.ui.viewmodels.LocationViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.LoginViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.LoginViewModelFactory
import com.senac.gerenciamentoviagens.ui.viewmodels.TaskViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.TaskViewModelFactory
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModelFactory

/**
 * Ponto de entrada principal da aplicação.
 * Gerencia o ciclo de vida básico e a configuração do tema global.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o layout de borda a borda (atrás das barras de sistema)
        setContent {
            GerenciamentoViagensTheme {
                MainScreen()
            }
        }
    }
}

/**
 * Composable principal que organiza a estrutura da UI, ViewModels globais e permissões.
 */
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    
    // Inicialização do Banco de Dados Singleton
    val database = remember { AppDatabase.getDatabase(context.applicationContext) }
    
    // Instanciação das ViewModels utilizando Factories para injeção de DAOs
    val taskFactory = remember { TaskViewModelFactory(database.taskDao()) }
    val taskViewModel: TaskViewModel = viewModel(factory = taskFactory)

    val loginFactory = remember { LoginViewModelFactory(database.userDao()) }
    val loginViewModel: LoginViewModel = viewModel(factory = loginFactory)

    val tripFactory = remember { TripViewModelFactory(database.tripDao(), database.userDao()) }
    val tripViewModel: TripViewModel = viewModel(factory = tripFactory)
    
    val locationViewModel: LocationViewModel = viewModel()

    // Monitora a descoberta de viagens ativas baseadas na localização e exibe feedback
    LaunchedEffect(tripViewModel.activeTripFound) {
        tripViewModel.activeTripFound?.let { trip ->
            Toast.makeText(context, "Viagem ativa encontrada: ${trip.destination}", Toast.LENGTH_LONG).show()
        }
    }

    // Configuração das permissões de localização (Fine e Coarse)
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Launcher para solicitação de múltiplas permissões em tempo de execução
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc || next }
        if (areGranted) {
            // Se concedido, solicita a localização e verifica viagens no banco
            locationViewModel.requestLocation(context) { city ->
                tripViewModel.checkForActiveTripInCity(city)
            }
        }
    }

    // Verifica permissões ao iniciar o Composable
    LaunchedEffect(Unit) {
        val hasPermissions = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (hasPermissions) {
            locationViewModel.requestLocation(context) { city ->
                tripViewModel.checkForActiveTripInCity(city)
            }
        } else {
            launcher.launch(permissions)
        }
    }

    // Configuração do Estado de Navegação (Navigation 3)
    val navigationState = rememberNavigationState(
        startRoute = Screen.Login,
        topLevelRoutes = setOf(Screen.Login, Screen.Menu(""))
    )
    val navigator = remember { Navigator(navigationState) }

    // Gerencia o botão "Voltar" físico do Android para fechar o app no Menu Principal
    if (navigationState.topLevelRoute is Screen.Menu) {
        val currentStack = navigationState.backStacks[navigationState.topLevelRoute]
        if (currentStack?.size == 1) {
            BackHandler {
                activity?.finish()
            }
        }
    }

    // Estrutura base com Scaffold e o Grafo de Navegação
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navigationState = navigationState,
                navigator = navigator,
                taskViewModel = taskViewModel,
                loginViewModel = loginViewModel,
                tripViewModel = tripViewModel,
                database = database
            )
        }
    }
}
