package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Interface de acesso a dados (DAO) para a entidade Photo.
 * Gerencia a persistência das referências de imagens vinculadas às viagens.
 */
@Dao
interface PhotoDao {
    /**
     * Insere uma nova referência de foto no banco de dados.
     */
    @Insert
    suspend fun insert(photo: Photo)

    /**
     * Retorna todas as fotos vinculadas a uma viagem específica.
     * @param tripId ID da viagem.
     * @return Flow reativo com a lista de fotos.
     */
    @Query("SELECT * FROM photos WHERE tripId = :tripId")
    fun getPhotosByTrip(tripId: Int): Flow<List<Photo>>

    /**
     * Remove uma foto do banco de dados.
     */
    @Delete
    suspend fun delete(photo: Photo)
}
