package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Upsert
    suspend fun upsert(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate DESC")
    fun getTripsByUser(userId: Int): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Int): Trip?
}
