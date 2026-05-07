package com.senac.gerenciamentoviagens.data

import androidx.room.TypeConverter
import com.senac.gerenciamentoviagens.data.model.TaskPriority
import com.senac.gerenciamentoviagens.data.model.TaskStatus
import com.senac.gerenciamentoviagens.data.model.TripType
import java.util.Date

class DatabaseConverters {
    @TypeConverter
    fun fromStatus(status: TaskStatus): Int = status.value

    @TypeConverter
    fun toStatus(value: Int): TaskStatus = TaskStatus.fromInt(value)

    @TypeConverter
    fun fromPriority(priority: TaskPriority): Int = priority.value

    @TypeConverter
    fun toPriority(value: Int): TaskPriority = TaskPriority.fromInt(value)

    @TypeConverter
    fun fromTripType(type: TripType): Int = type.value

    @TypeConverter
    fun toTripType(value: Int): TripType = TripType.fromInt(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)
}
