package br.edu.infnet.aula5.projetokotlin.Repository

import androidx.lifecycle.LiveData
import br.edu.infnet.aula5.projetokotlin.DAO.TarefaDao
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import com.google.firebase.database.DatabaseReference

class TarefaRepository(private val tarefaDao: TarefaDao, private val firebase : DatabaseReference) {
    val allTodos: LiveData<List<Tarefa>> = tarefaDao.getAllTodos()


    suspend fun getAllTodosExpired(): List<Tarefa>{
        return tarefaDao.getAllTodosExpired()
    }

    suspend fun insert(tarefa: Tarefa){
        val id = tarefaDao.insert(tarefa)
        tarefa.id = id.toInt()
        tarefa.status = tarefa.status!!.replace('"', ' ').trim()
        firebase.child("tarefas").child(id.toString()).setValue(tarefa)
    }

    suspend fun update(tarefa: Tarefa){
        tarefaDao.update(tarefa.id, tarefa.title, tarefa.note, tarefa.status)
        firebase.child("tarefas").child(tarefa.id.toString()).setValue(tarefa)
    }

    suspend fun delete (tarefa: Tarefa){
        tarefaDao.delete(tarefa.id)
        firebase.child("tarefas").child(tarefa.id.toString()).removeValue()
    }

}