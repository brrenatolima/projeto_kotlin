package br.edu.infnet.aula5.projetokotlin.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.infnet.aula5.projetokotlin.DAO.TarefaDao
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa

@Database(entities = arrayOf(Tarefa::class), version = 2)
abstract class TarefaDatabase : RoomDatabase() {

    abstract fun getTodoDao() : TarefaDao

    companion object {
        @Volatile
        private var INSTANCE : TarefaDatabase? = null

        fun getDatabase(context: Context): TarefaDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TarefaDatabase::class.java,
                    "tarefa"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}