package br.edu.infnet.aula5.projetokotlin.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.edu.infnet.aula5.projetokotlin.Database.TarefaDatabase
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.Repository.TarefaRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TarefaViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TarefaRepository
    val allTodo : LiveData<List<Tarefa>>

    init {
        val dao = TarefaDatabase.getDatabase(application).getTodoDao()
        val firebase = Firebase.database.reference
        repository = TarefaRepository(dao, firebase)
        allTodo = repository.allTodos
    }

    fun insertTarefa(tarefa: Tarefa) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(tarefa)
    }

    fun updateTodo(tarefa: Tarefa) = viewModelScope.launch(Dispatchers.IO){
        repository.update(tarefa)
    }

    fun deleteTodo(tarefa: Tarefa) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(tarefa)
    }
}