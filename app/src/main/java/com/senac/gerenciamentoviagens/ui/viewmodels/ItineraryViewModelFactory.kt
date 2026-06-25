package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagens.data.repository.GeminiRepository

class ItineraryViewModelFactory(private val repository: GeminiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItineraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItineraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
