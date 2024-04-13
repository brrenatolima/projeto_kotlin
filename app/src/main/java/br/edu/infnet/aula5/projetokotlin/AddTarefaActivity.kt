package br.edu.infnet.aula5.projetokotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.Fragments.ListaTarefasFragment
import br.edu.infnet.aula5.projetokotlin.Fragments.SaveButtonFragment
import br.edu.infnet.aula5.projetokotlin.Fragments.WeatherFragment
import br.edu.infnet.aula5.projetokotlin.databinding.ActivityAddTarefaBinding


class AddTarefaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTarefaBinding
    private lateinit var todo: Tarefa
    private lateinit var oldTodo: Tarefa
    var isUpdate = false
    private var isExpired : Boolean = true // Variável auxiliar para ser usada no click change do switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldTodo = intent.getSerializableExtra("tarefa", Tarefa::class.java) as Tarefa
            binding.etTitle.setText(oldTodo.title)
            binding.etNote.setText(oldTodo.note)
            binding.switch1.text = getString(R.string.expired_label)
            if (oldTodo.status == "ACTIVE"){
                isExpired = false
                binding.switch1.text = getString(R.string.active_label)
            }
            binding.switch1.isChecked = isExpired
            isUpdate = true
        } catch (e: Exception){
            e.printStackTrace()
        }

        supportFragmentManager.beginTransaction().replace(binding.fragmentoSavebutton.id, SaveButtonFragment()).commit()
        supportFragmentManager.beginTransaction().replace(binding.frameTodos.id, ListaTarefasFragment()).commit()

        binding.fragmentoSavebutton.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val todoDescription = binding.etNote.text.toString()

            // Se checkado = true, expirado
            var status = binding.switch1.isChecked
            var statusText : String

            if(status){
                statusText = "EXPIRED"
            } else {
                statusText = "ACTIVE"
            }

            if(title.isNotEmpty() && todoDescription.isNotEmpty()){

                if(isUpdate){
                    todo = Tarefa(oldTodo.id, title, todoDescription, statusText)
                }else{
                    todo = Tarefa(null, title, todoDescription, statusText)
                }
                var intent = Intent()
                intent.putExtra("todo", todo)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this@AddTarefaActivity, "Por favor digite", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }



        binding.imgDelete.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val todoDescription = binding.etNote.text.toString()
            todo = Tarefa(oldTodo.id, title, todoDescription)
            var intent = Intent()
            intent.putExtra("todo", todo)
            intent.putExtra("update", false)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }



        binding.imgBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            isExpired = isChecked
            binding.switch1.text =
                if (isExpired)
                    getString(R.string.expired_label)
                else
                getString(R.string.active_label)

        }

    }

}