package com.senac.gerenciamentoviagens.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidade que representa uma Foto vinculada a uma viagem.
 * Utiliza chave estrangeira para garantir a integridade referencial com a tabela de viagens.
 */
@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE // Remove as fotos automaticamente se a viagem for excluída
        )
    ]
)
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int, // ID da viagem à qual esta foto pertence
    val uri: String  // Caminho local ou URI da imagem no dispositivo
)
