package com.senac.gerenciamentoviagens.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.senac.gerenciamentoviagens.data.model.User

/**
 * Interface de acesso a dados (DAO) para a entidade User.
 */
@Dao
interface UserDao {
    /**
     * Insere um novo usuário no banco de dados.
     */
    @Insert
    suspend fun insert(user: User)

    /**
     * Busca um usuário pelo endereço de e-mail.
     * Utilizado para validação de login e verificação de duplicidade.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
