package br.edu.infnet.aula5.projetokotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import br.edu.infnet.aula5.projetokotlin.Account.LoginActivity
import br.edu.infnet.aula5.projetokotlin.Fragments.WeatherFragment
import br.edu.infnet.aula5.projetokotlin.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var filename: String? = null

    companion object {
        const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        initElement()
        initLauncher()

        supportFragmentManager.beginTransaction().replace(binding.fragmentoWeather.id, WeatherFragment()).commit()

    }

    fun backHome(view: View){
        val intent = Intent(this, ListarTarefaActivity::class.java)
        startActivity(intent)
        finish()
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


    private fun initElement() {
        binding.buttonPrint.setOnClickListener {
            takePicture()
        }
    }

    private fun initLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                if (it != null && it.resultCode == Activity.RESULT_OK) {
                    caseCapture()
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun caseCapture() {
        filename.let {
            if (it != null) {
                val tempFile = File(it)
                binding.image.setImageURI(tempFile.toUri())
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val image = createTempFile()

        val uri = FileProvider.getUriForFile(
            this,
            "br.edu.infnet.aula5.projetokotlin",
            image
        )

        filename = image.absolutePath.replace("/storage/emulated/0", "sdcard")

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        launcher.launch(intent)
    }

    private fun createTempFile(): File {
        val tempDir = File("${externalMediaDirs.first()}/Pictures")

        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }

        return File.createTempFile("capture_", ".png", tempDir)
    }
}