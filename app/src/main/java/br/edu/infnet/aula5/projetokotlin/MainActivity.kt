package br.edu.infnet.aula5.projetokotlin


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import br.edu.infnet.aula5.projetokotlin.Fragments.ListaTarefasFragment

import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="Welcome"

        auth = FirebaseAuth.getInstance()

        val btnTarefas = findViewById<ImageButton>(R.id.buttonNext)
        btnTarefas.setOnClickListener{
            startActivity(Intent(this, ListarTarefaActivity::class.java))
            finish()
        }

        val btnSettings = findViewById<ImageButton>(R.id.btn)
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }


}