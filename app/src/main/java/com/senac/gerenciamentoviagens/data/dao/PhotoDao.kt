package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photos WHERE tripId = :tripId")
    fun getPhotosByTrip(tripId: Int): Flow<List<Photo>>

    @Delete
    suspend fun delete(photo: Photo)
}
