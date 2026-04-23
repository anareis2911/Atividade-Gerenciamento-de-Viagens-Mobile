package com.senac.gerenciamentoviagens.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.senac.gerenciamentoviagens.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
