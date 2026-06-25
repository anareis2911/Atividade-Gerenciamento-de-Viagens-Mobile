package com.senac.gerenciamentoviagens.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidade que representa uma Viagem no banco de dados Room.
 */
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,               // ID auto-incremental gerado pelo Room
    val destination: String,        // Nome da cidade de destino
    val type: TripType,             // Tipo da viagem (Lazer ou Negócios)
    val startDate: Date,            // Data de início da viagem
    val endDate: Date,              // Data de término da viagem
    val budget: Double,             // Orçamento total previsto
    val userId: Int,                // ID do usuário proprietário desta viagem (chave estrangeira lógica)
    val interests: String = ""      // Interesses do usuário para geração de roteiro via IA
)
