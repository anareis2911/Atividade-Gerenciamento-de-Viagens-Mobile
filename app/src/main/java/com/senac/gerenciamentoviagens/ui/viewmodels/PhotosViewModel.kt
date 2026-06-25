package com.senac.gerenciamentoviagens.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.PhotoDao
import com.senac.gerenciamentoviagens.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PhotosViewModel(private val photoDao: PhotoDao) : ViewModel() {

    fun getPhotos(tripId: Int): Flow<List<Photo>> {
        return photoDao.getPhotosByTrip(tripId)
    }

    fun addPhoto(context: Context, tripId: Int, uri: Uri) {
        viewModelScope.launch {
            val internalUri = saveImageToInternalStorage(context, uri)
            photoDao.insert(Photo(tripId = tripId, uri = internalUri.toString()))
        }
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri): Uri {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "trip_photo_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return Uri.fromFile(file)
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            photoDao.delete(photo)
        }
    }
}
