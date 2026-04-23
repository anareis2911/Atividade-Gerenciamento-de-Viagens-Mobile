package com.senac.gerenciamentoviagens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagens.data.AppDatabase
import com.senac.gerenciamentoviagens.navigation.NavGraph
import com.senac.gerenciamentoviagens.navigation.Navigator
import com.senac.gerenciamentoviagens.navigation.Screen
import com.senac.gerenciamentoviagens.navigation.rememberNavigationState
import com.senac.gerenciamentoviagens.ui.theme.GerenciamentoViagensTheme
import com.senac.gerenciamentoviagens.ui.viewmodels.TaskViewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GerenciamentoViagensTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context.applicationContext) }
    val taskFactory = remember { TaskViewModelFactory(database.taskDao()) }
    
    val taskViewModel: TaskViewModel = viewModel(factory = taskFactory)

    val navigationState = rememberNavigationState(
        startRoute = Screen.Login,
        topLevelRoutes = setOf(Screen.Login)
    )
    val navigator = remember { Navigator(navigationState) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navigationState = navigationState,
                navigator = navigator,
                taskViewModel = taskViewModel,
                database = database
            )
        }
    }
}
