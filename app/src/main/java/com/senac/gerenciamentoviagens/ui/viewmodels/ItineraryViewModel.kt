package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.repository.GeminiRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewModel que gerencia a geração de roteiros de viagem utilizando IA.
 * Faz a ponte entre os dados da viagem e a API do Gemini.
 */
class ItineraryViewModel(private val repository: GeminiRepository) : ViewModel() {
    // Texto do roteiro gerado pela IA
    var itineraryText by mutableStateOf<String?>(null)
        private set

    // Estado de carregamento para controle de UI
    var isLoading by mutableStateOf(false)
        private set

    // Mensagem de erro caso a chamada à API falhe
    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Solicita ao Gemini a geração de um roteiro personalizado.
     * Formata os dados da viagem (destino, datas, interesses) para compor o prompt.
     */
    fun generateItinerary(trip: Trip) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val period = "${dateFormatter.format(trip.startDate)} até ${dateFormatter.format(trip.endDate)}"
        
        isLoading = true
        errorMessage = null
        
        viewModelScope.launch {
            // Chama o repositório para processar a requisição de IA
            val result = repository.generateItinerary(
                destination = trip.destination,
                period = period,
                interests = if (trip.interests.isNotBlank()) trip.interests else (if (trip.type == com.senac.gerenciamentoviagens.data.model.TripType.LEISURE) "Lazer e Turismo" else "Negócios e Networking")
            )
            
            if (result != null) {
                itineraryText = result
            } else {
                errorMessage = "Falha ao gerar roteiro. Verifique sua conexão e a chave de API."
            }
            isLoading = false
        }
    }
}
