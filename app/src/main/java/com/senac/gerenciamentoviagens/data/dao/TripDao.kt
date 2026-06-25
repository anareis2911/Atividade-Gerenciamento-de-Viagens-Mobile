package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Trip
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Interface de acesso a dados (DAO) para a entidade Trip (Viagens).
 */
@Dao
interface TripDao {
    /**
     * Insere ou atualiza uma viagem.
     * Se o ID já existir, substitui os dados existentes.
     */
    @Upsert
    suspend fun upsert(trip: Trip)

    /**
     * Remove uma viagem específica do banco de dados.
     */
    @Delete
    suspend fun delete(trip: Trip)

    /**
     * Retorna todas as viagens de um usuário específico, ordenadas pela data de início (mais recentes primeiro).
     * @param userId ID do usuário logado.
     * @return Flow reativo com a lista de viagens.
     */
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate DESC")
    fun getTripsByUser(userId: Int): Flow<List<Trip>>

    /**
     * Busca os detalhes de uma viagem específica pelo seu ID.
     */
    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Int): Trip?

    /**
     * Busca uma viagem ativa para uma determinada cidade na data atual.
     * Realiza a comparação ignorando maiúsculas e minúsculas (LOWER).
     * Verifica se a data atual está entre a data de início e fim da viagem.
     */
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
