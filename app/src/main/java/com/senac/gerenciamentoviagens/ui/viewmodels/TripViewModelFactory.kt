package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagens.data.dao.TripDao
import com.senac.gerenciamentoviagens.data.dao.UserDao

class TripViewModelFactory(
    private val tripDao: TripDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(tripDao, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
