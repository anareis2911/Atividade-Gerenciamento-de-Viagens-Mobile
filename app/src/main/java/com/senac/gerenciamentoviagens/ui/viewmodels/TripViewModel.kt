package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.TripDao
import com.senac.gerenciamentoviagens.data.dao.UserDao
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.model.TripType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.util.Date

class TripViewModel(
    private val tripDao: TripDao,
    private val userDao: UserDao
) : ViewModel() {

    var destination by mutableStateOf("")
    var type by mutableStateOf(TripType.LEISURE)
    var startDate by mutableStateOf<Long?>(null)
    var endDate by mutableStateOf<Long?>(null)
    var budget by mutableStateOf("")
    
    var showErrors by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    var activeTripFound by mutableStateOf<Trip?>(null)
        private set

    private var currentUserId: Int = -1
    private var detectedCity: String? = null

    fun setUserIdByEmail(email: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                currentUserId = user.id
                // Se já temos uma cidade detectada, busca a viagem agora que temos o ID
                detectedCity?.let { checkForActiveTripInCity(it) }
            }
        }
    }

    fun getTrips(): Flow<List<Trip>> {
        return if (currentUserId != -1) {
            tripDao.getTripsByUser(currentUserId)
        } else {
            emptyFlow()
        }
    }

    fun checkForActiveTripInCity(city: String) {
        detectedCity = city
        if (currentUserId == -1) return
        
        viewModelScope.launch {
            val trip = tripDao.getActiveTripByCity(currentUserId, city, Date())
            activeTripFound = trip
        }
    }

    fun saveTrip(onSuccess: () -> Unit) {
        showErrors = true
        val budgetDouble = budget.toDoubleOrNull()
        
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
                userId = currentUserId
            )
            tripDao.upsert(trip)
            successMessage = "Viagem salva com sucesso!"
            
            // Se salvou uma viagem para a cidade onde o usuário está, atualiza o card de viagem ativa
            detectedCity?.let { checkForActiveTripInCity(it) }
            
            onSuccess()
            clearFields()
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.delete(trip)
            if (activeTripFound?.id == trip.id) {
                activeTripFound = null
            }
        }
    }

    private fun clearFields() {
        destination = ""
        type = TripType.LEISURE
        startDate = null
        endDate = null
        budget = ""
        showErrors = false
        errorMessage = null
    }
}
