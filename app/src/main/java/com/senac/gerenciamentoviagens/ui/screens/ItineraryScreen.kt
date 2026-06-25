package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.ui.viewmodels.ItineraryViewModel

/**
 * Tela que exibe o roteiro gerado por Inteligência Artificial (Gemini).
 * Recebe os detalhes da viagem e solicita a geração do texto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    tripId: Int,
    trip: Trip?,
    viewModel: ItineraryViewModel,
    onBack: () -> Unit
) {
    // Dispara a geração do roteiro assim que a viagem é carregada, se ainda não houver texto
    LaunchedEffect(trip) {
        if (trip != null && viewModel.itineraryText == null) {
            viewModel.generateItinerary(trip)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roteiro Inteligente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (trip == null) {
                Text("Erro: Viagem não encontrada.")
            } else {
                Text(
                    text = "Roteiro para ${trip.destination}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Estado de Carregamento
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                    Text("O Gemini está criando seu roteiro...")
                } 
                // Estado de Erro
                else if (viewModel.errorMessage != null) {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = { viewModel.generateItinerary(trip) }) {
                        Text("Tentar Novamente")
                    }
                } 
                // Exibição do Roteiro
                else {
                    viewModel.itineraryText?.let { text ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = text,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
