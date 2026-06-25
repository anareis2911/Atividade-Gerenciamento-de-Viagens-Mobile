package com.senac.gerenciamentoviagens.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidade que representa uma Tarefa ou Atividade vinculada a uma viagem.
 * Utilizada para gerenciar o checklist de coisas a fazer durante o percurso.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,               // Identificador único da tarefa
    val title: String,             // Título ou nome da atividade
    val description: String,       // Detalhes adicionais sobre a tarefa
    val status: TaskStatus = TaskStatus.PENDING, // Estado atual (Pendente ou Concluída)
    val priority: TaskPriority = TaskPriority.MEDIUM, // Nível de importância
    val dateTime: Date = Date()    // Data e hora programada para a tarefa
)
