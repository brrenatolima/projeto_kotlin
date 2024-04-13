package br.edu.infnet.aula5.projetokotlin.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa

@Dao
interface TarefaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarefa : Tarefa) : Long

    @Query("SELECT * FROM todo ORDER BY id ASC")
    fun getAllTodos() : LiveData<List<Tarefa>>

    @Query("SELECT * FROM todo WHERE status = 'EXPIRED' ORDER BY id ASC")
    suspend fun getAllTodosExpired() : List<Tarefa>

    @Query("UPDATE todo SET title = :title, description = :description, status = :status WHERE id = :id")
    suspend fun update(id : Int?, title : String?, description : String?, status : String?)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun delete(id: Int?)
}