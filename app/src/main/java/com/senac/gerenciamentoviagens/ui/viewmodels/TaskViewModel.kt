package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.TaskDao
import com.senac.gerenciamentoviagens.data.model.Task
import com.senac.gerenciamentoviagens.data.model.TaskPriority
import com.senac.gerenciamentoviagens.data.model.TaskStatus
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var status by mutableStateOf(TaskStatus.PENDING)
    var priority by mutableStateOf(TaskPriority.MEDIUM)
    var dateTime by mutableStateOf(Date())

    fun onTitleChange(newValue: String) { title = newValue }
    fun onDescriptionChange(newValue: String) { description = newValue }
    fun onStatusChange(newValue: TaskStatus) { status = newValue }
    fun onPriorityChange(newValue: TaskPriority) { priority = newValue }
    fun onDateTimeChange(newValue: Date) { dateTime = newValue }

    fun submit() {
        viewModelScope.launch {
            val task = Task(
                title = title,
                description = description,
                status = status,
                priority = priority,
                dateTime = dateTime
            )
            taskDao.upsert(task)
        }
    }
}
