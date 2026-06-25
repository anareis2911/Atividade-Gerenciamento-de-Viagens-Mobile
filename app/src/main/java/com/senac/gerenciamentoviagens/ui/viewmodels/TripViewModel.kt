package com.senac.gerenciamentoviagens.ui.viewmodels

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.senac.gerenciamentoviagens.data.dao.TripDao
import com.senac.gerenciamentoviagens.data.dao.UserDao
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.model.TripType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel que gerencia a lógica de negócios das viagens.
 * Responsável por cadastrar, listar, excluir e identificar a viagem ativa do usuário.
 */
class TripViewModel(
    private val tripDao: TripDao,
    private val userDao: UserDao
) : ViewModel() {

    // Estados observáveis para o formulário de nova viagem
    var destination by mutableStateOf("")
    var type by mutableStateOf(TripType.LEISURE)
    var startDate by mutableStateOf<Long?>(null)
    var endDate by mutableStateOf<Long?>(null)
    var budget by mutableStateOf("")
    var interests by mutableStateOf("")
    
    // Estados para controle de feedback e erros
    var showErrors by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // Armazena a viagem ativa encontrada para o local/data atual
    var activeTripFound by mutableStateOf<Trip?>(null)
        private set
        
    // Armazena as coordenadas geográficas da viagem ativa para exibir no mapa
    var activeTripLatLng by mutableStateOf<LatLng?>(null)
        private set

    private var currentUserId: Int = -1
    private var detectedCity: String? = null

    /**
     * Define o ID do usuário baseado no e-mail logado.
     * Necessário para filtrar as viagens do banco de dados.
     */
    fun setUserIdByEmail(email: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                currentUserId = user.id
                // Re-verifica se há viagens para a cidade detectada assim que o usuário loga
                detectedCity?.let { checkForActiveTripInCity(it) }
            }
        }
    }

    /**
     * Retorna um Flow com a lista de viagens do usuário logado.
     */
    fun getTrips(): Flow<List<Trip>> {
        return if (currentUserId != -1) {
            tripDao.getTripsByUser(currentUserId)
        } else {
            emptyFlow()
        }
    }

    /**
     * Verifica se existe uma viagem no banco para a cidade detectada e data atual.
     */
    fun checkForActiveTripInCity(city: String) {
        detectedCity = city
        if (currentUserId == -1) return
        
        viewModelScope.launch {
            // Consulta o banco buscando viagem que coincida com a cidade e o período atual
            val trip = tripDao.getActiveTripByCity(currentUserId, city, Date())
            activeTripFound = trip
        }
    }
    
    /**
     * Converte o nome do destino em coordenadas (Latitude/Longitude) usando Geocoding.
     */
    fun geocodeDestination(context: Context, tripDestination: String) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(tripDestination, 1)
                val address = addresses?.firstOrNull()
                if (address != null) {
                    activeTripLatLng = LatLng(address.latitude, address.longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Salva ou atualiza uma viagem no banco de dados.
     */
    fun saveTrip(onSuccess: () -> Unit) {
        showErrors = true
        val budgetDouble = budget.toDoubleOrNull()
        
        // Validação de campos obrigatórios
        if (destination.isBlank() || startDate == null || endDate == null || budget.isBlank() || budgetDouble == null) {
            errorMessage = "Todos os campos são obrigatórios e orçamento deve ser um número"
            return
        }

        if (currentUserId == -1) {
            errorMessage = "Erro: Usuário não identificado"
            return
        }

        viewModelScope.launch {
            val trip = Trip(
                destination = destination,
                type = type,
                startDate = Date(startDate!!),
                endDate = Date(endDate!!),
                budget = budgetDouble,
                userId = currentUserId,
                interests = interests
            )
            tripDao.upsert(trip)
            successMessage = "Viagem salva com sucesso!"
            // Atualiza a verificação de viagem ativa após salvar
            detectedCity?.let { checkForActiveTripInCity(it) }
            onSuccess()
            clearFields()
        }
    }

    /**
     * Exclui uma viagem do banco de dados.
     */
    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.delete(trip)
            // Se a viagem excluída era a ativa, limpa o estado
            if (activeTripFound?.id == trip.id) {
                activeTripFound = null
                activeTripLatLng = null
            }
        }
    }

    /**
     * Limpa os campos do formulário.
     */
    private fun clearFields() {
        destination = ""
        type = TripType.LEISURE
        startDate = null
        endDate = null
        budget = ""
        interests = ""
        showErrors = false
        errorMessage = null
    }
}
