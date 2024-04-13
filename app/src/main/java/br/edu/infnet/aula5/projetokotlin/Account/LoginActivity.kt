package br.edu.infnet.aula5.projetokotlin.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import br.edu.infnet.aula5.projetokotlin.ListarTarefaActivity
import br.edu.infnet.aula5.projetokotlin.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="Login"

        auth= FirebaseAuth.getInstance()
    }

    fun login(v: View){
        val email= this.findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password=this.findViewById<EditText>(R.id.editTextPassword).text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this, ListarTarefaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun goToRegister(v: View){
        val intent= Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun goToRecovery(view: View) {
        val intent = Intent(this, RecoveryActivity::class.java)
        startActivity(intent)
    }
}