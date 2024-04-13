package br.edu.infnet.aula5.projetokotlin.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle incoming FCM message
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // Create and show a notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "Channel_ID")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("Channel_ID", "Channel_Name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // Handle new or refreshed FCM registration token
        Log.d("teste", "teste")
        Log.d(TAG, "Refreshed token: $token")
        // You may want to send this token to your server for further use
    }


    private val TAG = "MyFirebaseMsgService"

}