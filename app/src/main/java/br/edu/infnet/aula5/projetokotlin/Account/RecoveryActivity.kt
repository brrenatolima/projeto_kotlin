package br.edu.infnet.aula5.projetokotlin.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import br.edu.infnet.aula5.projetokotlin.databinding.ActivityRecoveryBinding
import com.google.firebase.auth.FirebaseAuth

class RecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecoveryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        binding.buttonRecovery.setOnClickListener {
            val email: String = binding.editTextEmailAddress.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                Toast.makeText(this@RecoveryActivity,"Informe um e-mail para recuperar a senha!", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        binding.textViewPassRecovered.visibility = VISIBLE
                        Toast.makeText(this@RecoveryActivity,binding.textViewPassRecovered.text, Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        binding.textViewPassRecovered.visibility = VISIBLE
                        binding.textViewPassRecovered.text = task.exception!!.message.toString()
                    }
                }
            }
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}