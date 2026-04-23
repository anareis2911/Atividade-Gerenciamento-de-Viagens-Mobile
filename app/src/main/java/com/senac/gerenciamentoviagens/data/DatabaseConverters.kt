package com.senac.gerenciamentoviagens.data

import androidx.room.TypeConverter
import com.senac.gerenciamentoviagens.data.model.TaskPriority
import com.senac.gerenciamentoviagens.data.model.TaskStatus
import java.util.Date

class DatabaseConverters {
    @TypeConverter
    fun fromStatus(status: TaskStatus): Int {
        return status.value
    }

    @TypeConverter
    fun toStatus(value: Int): TaskStatus {
        return TaskStatus.fromInt(value)
    }

    @TypeConverter
    fun fromPriority(priority: TaskPriority): Int {
        return priority.value
    }

    @TypeConverter
    fun toPriority(value: Int): TaskPriority {
        return TaskPriority.fromInt(value)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }
}
