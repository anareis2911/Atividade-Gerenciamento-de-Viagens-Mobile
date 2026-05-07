package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(email: String, tripViewModel: TripViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Nova Viagem") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Flight, contentDescription = null) },
                    label = { Text("Nova viagem ✈️") },
                    selected = selectedItem == "Nova Viagem",
                    onClick = {
                        selectedItem = "Nova Viagem"
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Luggage, contentDescription = null) },
                    label = { Text("Minhas Viagens 🧳") },
                    selected = selectedItem == "Minhas Viagens",
                    onClick = {
                        selectedItem = "Minhas Viagens"
                        scope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text("Sobre ℹ️") },
                    selected = selectedItem == "Sobre",
                    onClick = {
                        selectedItem = "Sobre"
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedItem) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when (selectedItem) {
                    "Nova Viagem" -> NewTripScreen(viewModel = tripViewModel)
                    "Minhas Viagens" -> MyTripsScreen(
                        viewModel = tripViewModel,
                        onNavigateToEdit = { selectedItem = "Nova Viagem" }
                    )
                    "Sobre" -> AboutScreen()
                }
            }
        }
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("Gerenciamento de Viagens", style = MaterialTheme.typography.headlineMedium)
        Text("Desenvolvido para o Trabalho Final", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Versão 1.0.0")
    }
}
