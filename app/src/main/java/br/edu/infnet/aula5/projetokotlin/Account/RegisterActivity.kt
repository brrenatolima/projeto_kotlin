package br.edu.infnet.aula5.projetokotlin.Account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.infnet.aula5.projetokotlin.ListarTarefaActivity
import br.edu.infnet.aula5.projetokotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var buttonRegister: Button
    private lateinit var editTextEmail : EditText
    private lateinit var editTextPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="Register"

        auth= FirebaseAuth.getInstance()

        editTextPassword = binding.editTextPassword
        editTextEmail = binding.editTextEmailAddress
        buttonRegister = binding.buttonRegister

        editTextPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                passwordValidate()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailValidate()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun emailValidate(){
        var safetyEmail = emailValidateRegex(editTextEmail.text.toString())
        if (safetyEmail){
            buttonRegister.isEnabled = true
            binding.safetyPassword.text = "E-mail valido!"
            binding.safetyPassword.setTextColor(Color.GREEN)
        } else {
            buttonRegister.isEnabled = false
            binding.safetyPassword.setTextColor(Color.RED)
            binding.safetyPassword.text = "E-mail inválido!"
        }
    }

    private fun passwordValidate() {
        var safetyPass = passwordValidateRegex(editTextPassword.text.toString())
        val safetyPassDescription = when (safetyPass) {
            0 -> "Senha Fraca"
            5 -> "Senha Mediana"
            10 -> "Senha Forte"
            else -> "Outro"
        }
        binding.safetyPassword.text = safetyPassDescription

        if (safetyPass == 0){
            buttonRegister.isEnabled = false
            binding.safetyPassword.setTextColor(Color.RED)
        } else if (safetyPass == 5){
            buttonRegister.isEnabled = true
            binding.safetyPassword.setTextColor(Color.YELLOW)
        } else {
            buttonRegister.isEnabled = true
            binding.safetyPassword.setTextColor(Color.GREEN)
        }
    }

    fun register(view: View){
        val email = binding.editTextEmailAddress.text.toString() //this.findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password = binding.editTextPassword.text.toString() //this.findViewById<EditText>(R.id.editTextPassword).text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this, ListarTarefaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    private fun passwordValidateRegex(senha: String): Int {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$")
        return when {
            regex.matches(senha) -> 10//"Senha forte"
            senha.length >= 8 -> 5 //"Senha média"
            else -> 0 //"Senha fraca"
        }
    }

    fun emailValidateRegex(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return regex.matches(email)
    }


}