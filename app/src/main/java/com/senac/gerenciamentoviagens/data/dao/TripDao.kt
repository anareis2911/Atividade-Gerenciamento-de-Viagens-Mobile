package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Trip
import kotlinx.coroutines.flow.Flow
import java.util.Date

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

    @Query("""
        SELECT * FROM trips 
        WHERE userId = :userId 
        AND LOWER(destination) = LOWER(:city) 
        AND :currentDate >= startDate 
        AND :currentDate <= endDate 
        LIMIT 1
    """)
    suspend fun getActiveTripByCity(userId: Int, city: String, currentDate: Date): Trip?
}
