package br.edu.infnet.aula5.projetokotlin.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaveButtonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveButtonFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var isUpdate = false
    private lateinit var view: View
    private lateinit var todo: Tarefa
    private lateinit var oldTodo: Tarefa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_save_button, container, false)


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaveButtonFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveButtonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)


/*
                    view.findViewById<ImageView>(R.id.img_check).setOnClickListener {
                        val title = view.findViewById<TextView>(R.id.et_title).toString() //binding.etTitle.text.toString()
                        val todoDescription = view.findViewById<TextView>(R.id.et_note).toString() // binding.etNote.text.toString()

                        // Se checkado = true, expirado
                        var status = view.findViewById<Switch>(R.id.switch1).isChecked//binding.switch1.isChecked
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
                    */
                }
            }
    }
}