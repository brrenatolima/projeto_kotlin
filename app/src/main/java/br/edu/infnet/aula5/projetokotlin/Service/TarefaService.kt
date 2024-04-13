package br.edu.infnet.aula5.projetokotlin.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import br.edu.infnet.aula5.projetokotlin.Database.TarefaDatabase
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.Repository.TarefaRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TarefaService : Service() {


    private lateinit var repository: TarefaRepository
    private lateinit var allTodoExpired : List<Tarefa>
    private lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        // O service não tem contexto de aplicação, ao contrário da Activity
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dao = TarefaDatabase.getDatabase(appContext).getTodoDao()
        val firebase = Firebase.database.reference
        repository = TarefaRepository(dao, firebase)
        CoroutineScope(Dispatchers.IO).launch{
            allTodoExpired = repository.getAllTodosExpired()

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("Todo_channel", "Channel_Name",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            var body = ""
            for (item in allTodoExpired){
                // Access item properties
                val note = item.note
                val title = item.title

                // Perform operations using item data
                body = "$body $title $note\n"
            }

            val notificationBuilder = NotificationCompat.Builder(appContext, "Todo_channel")
                .setContentTitle("Tarefas expiradas")
                .setContentText(body)
                .setSmallIcon(androidx.core.R.drawable.notification_bg)
                .setAutoCancel(true)

            notificationManager.notify(1, notificationBuilder.build())
        }

        return super.onStartCommand(intent, flags, startId)
    }
}