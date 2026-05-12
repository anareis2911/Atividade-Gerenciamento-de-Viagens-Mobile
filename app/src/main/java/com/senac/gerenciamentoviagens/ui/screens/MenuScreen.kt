package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.model.TripType
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(email: String, tripViewModel: TripViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Início") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Início 🏠") },
                    selected = selectedItem == "Início",
                    onClick = {
                        selectedItem = "Início"
                        scope.launch { drawerState.close() }
                    }
                )
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
                    "Início" -> HomeScreen(email, tripViewModel)
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
fun HomeScreen(email: String, tripViewModel: TripViewModel) {
    val activeTrip = tripViewModel.activeTripFound

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bem-vindo!", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Usuário: $email", style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.height(24.dp))

        if (activeTrip != null) {
            ActiveTripCard(trip = activeTrip)
        } else {
            Text(text = "Nenhuma viagem ativa para sua localização atual.", color = Color.Gray)
        }
    }
}

@Composable
fun ActiveTripCard(trip: Trip) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📍 Viagem Atual Encontrada!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text(text = "Destino: ${trip.destination}", fontWeight = FontWeight.SemiBold)
            Text(text = "Tipo: ${if (trip.type == TripType.LEISURE) "Lazer 🏖️" else "Negócios 💼"}")
            Text(text = "Período: ${dateFormatter.format(trip.startDate)} até ${dateFormatter.format(trip.endDate)}")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Orçamento 💰", style = MaterialTheme.typography.labelSmall)
                    Text(text = "R$ ${String.format(Locale.getDefault(), "%.2f", trip.budget)}", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Gastos 📉", style = MaterialTheme.typography.labelSmall)
                    Text(text = "R$ 0,00", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gerenciamento de Viagens", style = MaterialTheme.typography.headlineMedium)
        Text("Desenvolvido para o Trabalho Final", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Versão 1.0.0")
    }
}
