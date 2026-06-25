package com.senac.gerenciamentoviagens.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senac.gerenciamentoviagens.data.dao.PhotoDao
import com.senac.gerenciamentoviagens.data.dao.TaskDao
import com.senac.gerenciamentoviagens.data.dao.TripDao
import com.senac.gerenciamentoviagens.data.dao.UserDao
import com.senac.gerenciamentoviagens.data.model.Photo
import com.senac.gerenciamentoviagens.data.model.Task
import com.senac.gerenciamentoviagens.data.model.Trip
import com.senac.gerenciamentoviagens.data.model.User

/**
 * Configuração principal do Banco de Dados Room.
 * Define as entidades, versão e conversores de tipo.
 */
@Database(entities = [Task::class, User::class, Trip::class, Photo::class], version = 4, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAOs (Data Access Objects) para cada entidade
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retorna a instância única do banco de dados (Singleton).
         * Utiliza synchronized para evitar criação duplicada em múltiplas threads.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TravelDB"
                )
                // Permite migrações destrutivas durante a fase de desenvolvimento
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
