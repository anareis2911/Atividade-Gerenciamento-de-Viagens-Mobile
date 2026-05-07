package com.senac.gerenciamentoviagens.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destination: String,
    val type: TripType,
    val startDate: Date,
    val endDate: Date,
    val budget: Double,
    val userId: Int
)
