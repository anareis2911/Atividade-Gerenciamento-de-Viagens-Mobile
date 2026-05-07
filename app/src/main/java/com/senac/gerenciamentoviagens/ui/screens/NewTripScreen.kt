package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senac.gerenciamentoviagens.data.model.TripType
import com.senac.gerenciamentoviagens.ui.viewmodels.TripViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(viewModel: TripViewModel) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = viewModel.destination,
            onValueChange = { viewModel.destination = it },
            label = { Text("Destino 📍") },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.showErrors && viewModel.destination.isBlank()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tipo de Viagem", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = viewModel.type == TripType.LEISURE,
                onClick = { viewModel.type = TripType.LEISURE }
            )
            Text("Lazer 🏖️")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = viewModel.type == TripType.BUSINESS,
                onClick = { viewModel.type = TripType.BUSINESS }
            )
            Text("Negócios 💼")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showStartDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.startDate == null) "Data Início 📅" else "Início: ${dateFormatter.format(Date(viewModel.startDate!!))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showEndDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.endDate == null) "Data Fim 📅" else "Fim: ${dateFormatter.format(Date(viewModel.endDate!!))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.budget,
            onValueChange = { viewModel.budget = it },
            label = { Text("Orçamento 💰") },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.showErrors && (viewModel.budget.isBlank() || viewModel.budget.toDoubleOrNull() == null)
        )

        viewModel.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.saveTrip { } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Salvar Viagem")
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.startDate = datePickerState.selectedDateMillis
                    showStartDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.endDate = datePickerState.selectedDateMillis
                    showEndDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }
}
