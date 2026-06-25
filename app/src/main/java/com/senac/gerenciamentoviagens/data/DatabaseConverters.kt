package com.senac.gerenciamentoviagens.data

import androidx.room.TypeConverter
import com.senac.gerenciamentoviagens.data.model.TaskPriority
import com.senac.gerenciamentoviagens.data.model.TaskStatus
import com.senac.gerenciamentoviagens.data.model.TripType
import java.util.Date

/**
 * Conversores de tipo para o Room.
 * Permitem que o banco de dados armazene tipos complexos (Enums, Date) como tipos primitivos (Int, Long).
 */
class DatabaseConverters {
    // Conversão de Status da Tarefa
    @TypeConverter
    fun fromStatus(status: TaskStatus): Int = status.value

    @TypeConverter
    fun toStatus(value: Int): TaskStatus = TaskStatus.fromInt(value)

    // Conversão de Prioridade da Tarefa
    @TypeConverter
    fun fromPriority(priority: TaskPriority): Int = priority.value

    @TypeConverter
    fun toPriority(value: Int): TaskPriority = TaskPriority.fromInt(value)

    // Conversão de Tipo de Viagem
    @TypeConverter
    fun fromTripType(type: TripType): Int = type.value

    @TypeConverter
    fun toTripType(value: Int): TripType = TripType.fromInt(value)

    // Conversão de Data (java.util.Date para Long/Timestamp)
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)
}
