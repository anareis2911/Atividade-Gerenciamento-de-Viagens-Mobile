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

/**
 * ViewModel responsável pela gestão das fotos de uma viagem.
 * Lida com a persistência de imagens no armazenamento interno e no banco de dados.
 */
class PhotosViewModel(private val photoDao: PhotoDao) : ViewModel() {

    /**
     * Retorna um Flow contendo a lista de fotos associadas a uma viagem.
     */
    fun getPhotos(tripId: Int): Flow<List<Photo>> {
        return photoDao.getPhotosByTrip(tripId)
    }

    /**
     * Adiciona uma nova foto à viagem.
     * Copia a imagem da URI original para o armazenamento interno do app para garantir persistência.
     */
    fun addPhoto(context: Context, tripId: Int, uri: Uri) {
        viewModelScope.launch {
            val internalUri = saveImageToInternalStorage(context, uri)
            photoDao.insert(Photo(tripId = tripId, uri = internalUri.toString()))
        }
    }

    /**
     * Copia o arquivo de imagem para o diretório de arquivos internos do aplicativo.
     * @return URI do novo arquivo salvo internamente.
     */
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

    /**
     * Remove uma foto do banco de dados (o arquivo físico pode ser mantido ou removido).
     */
    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            photoDao.delete(photo)
        }
    }
}
