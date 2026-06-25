package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagens.data.dao.PhotoDao

class PhotosViewModelFactory(private val photoDao: PhotoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotosViewModel(photoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
