package com.senac.gerenciamentoviagens.data.dao

import androidx.room.*
import com.senac.gerenciamentoviagens.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Interface de acesso a dados (DAO) para a entidade Task.
 * Gerencia as operações de checklist e atividades das viagens.
 */
@Dao
interface TaskDao {

    /**
     * Insere uma nova tarefa. Se houver conflito, substitui a existente.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    /**
     * Insere ou atualiza uma tarefa de forma inteligente.
     */
    @Upsert
    suspend fun upsert(task: Task)

    /**
     * Atualiza os dados de uma tarefa existente.
     */
    @Update
    suspend fun update(task: Task)

    /**
     * Remove uma tarefa do banco de dados.
     */
    @Delete
    suspend fun delete(task: Task)

    /**
     * Retorna todas as tarefas cadastradas como um fluxo de dados observável.
     */
    @Query("SELECT * FROM tasks")
    fun findAll(): Flow<List<Task>>
}
