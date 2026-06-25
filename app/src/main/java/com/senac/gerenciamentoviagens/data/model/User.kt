package com.senac.gerenciamentoviagens.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa um Usuário no sistema.
 * Armazena informações de perfil e credenciais de acesso.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,        // Identificador único do usuário
    val name: String,      // Nome completo do usuário
    val email: String,     // E-mail (utilizado como login)
    val phone: String,     // Telefone de contato
    val password: String   // Senha de acesso
)
