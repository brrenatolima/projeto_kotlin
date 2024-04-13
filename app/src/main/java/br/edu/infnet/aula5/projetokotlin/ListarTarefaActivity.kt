package br.edu.infnet.aula5.projetokotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.aula5.projetokotlin.Account.LoginActivity
import br.edu.infnet.aula5.projetokotlin.Adapter.TarefaAdapter
import br.edu.infnet.aula5.projetokotlin.Database.TarefaDatabase
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.Service.TarefaService
import br.edu.infnet.aula5.projetokotlin.ViewModel.TarefaViewModel
import br.edu.infnet.aula5.projetokotlin.databinding.ActivityListarTarefaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp


class ListarTarefaActivity : AppCompatActivity(), TarefaAdapter.TodoClickListener {

    private lateinit var binding: ActivityListarTarefaBinding
    private lateinit var database: TarefaDatabase
    lateinit var viewModel: TarefaViewModel
    lateinit var adapter: TarefaAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Serviços */
        FirebaseApp.initializeApp(this)

        val serviceIntent = Intent(this, TarefaService::class.java)
        startService(serviceIntent)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseMessaging", "Erro ao registrar token", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            val msg = "token"
            Log.i("Token", token)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        /* Fim Serviços */

        binding = ActivityListarTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        initUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(TarefaViewModel::class.java)

        viewModel.allTodo.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = TarefaDatabase.getDatabase(this)


    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TarefaAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val todo = result.data?.getSerializableExtra("todo", Tarefa::class.java) as? Tarefa
                    if (todo != null) {
                        viewModel.insertTarefa(todo)
                    }
                }
            }

        binding.fabAddTodo.setOnClickListener {
            val intent = Intent(this, AddTarefaActivity::class.java)
            getContent.launch(intent)
        }

    }

    private val updateTodo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val todo = result.data?.getSerializableExtra("todo", Tarefa::class.java) as Tarefa
                val update = result.data?.getBooleanExtra("update", true)
                if(update == true){
                    viewModel.updateTodo(todo)
                }
                viewModel.deleteTodo(todo)
            }
        }



    override fun onItemClicked(tarefa: Tarefa) {
        val intent = Intent(this@ListarTarefaActivity, AddTarefaActivity::class.java)
        intent.putExtra("tarefa", tarefa)
        updateTodo.launch(intent)
    }

    fun signOut(view: View) {

        var modal = AlertDialog.Builder(this)
        modal.setTitle("Logout")
        modal.setMessage("Tem certeza que deseja fazer logout?")
        modal.setPositiveButton("Sim", DialogInterface.OnClickListener { dialog, id ->
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            dialog.dismiss()
            startActivity(intent)
            finish()
        })

        modal.create()
        modal.show()

    }

    fun goToSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }
}