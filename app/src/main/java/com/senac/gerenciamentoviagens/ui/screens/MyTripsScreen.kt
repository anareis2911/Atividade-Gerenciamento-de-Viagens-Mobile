package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.model.TripType
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyTripsScreen(viewModel: TripViewModel, onNavigateToEdit: () -> Unit) {
    val trips by viewModel.getTrips().collectAsState(initial = emptyList())
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (trips.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhuma viagem cadastrada.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = trips,
                key = { it.id }
            ) { trip ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                            viewModel.deleteTrip(trip)
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.Settled -> Color.Transparent
                                else -> Color.Red.copy(alpha = 0.5f)
                            }, label = "dismissColor"
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = Color.White
                            )
                        }
                    },
                    content = {
                        TripItem(
                            trip = trip,
                            onEdit = {
                                viewModel.destination = trip.destination
                                viewModel.type = trip.type
                                viewModel.startDate = trip.startDate.time
                                viewModel.endDate = trip.endDate.time
                                viewModel.budget = trip.budget.toString()
                                onNavigateToEdit()
                            },
                            dateFormatter = dateFormatter
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripItem(
    trip: Trip,
    onEdit: () -> Unit,
    dateFormatter: SimpleDateFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Opcional: ver detalhes */ },
                onLongClick = onEdit
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: ImageVector = if (trip.type == TripType.LEISURE) {
                Icons.Default.BeachAccess
            } else {
                Icons.Default.Business
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = trip.destination, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${dateFormatter.format(trip.startDate)} - ${dateFormatter.format(trip.endDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Orçamento: R$ ${String.format(Locale.getDefault(), "%.2f", trip.budget)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
