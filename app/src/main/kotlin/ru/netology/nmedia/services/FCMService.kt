package ru.netology.nmedia.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.repository.AuthManager
import ru.netology.nmedia.presentation.activities.MainActivity
import ru.netology.nmedia.services.dto.Notify
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    @Inject
    lateinit var authManager: AuthManager

    override fun onMessageReceived(message: RemoteMessage) {

        log(message.data)

        val notify: Notify = gson.fromJson(message.data[content], Notify::class.java)

        log(notify)

        val myId = authManager.getId()
        val recipientId = notify.recipientId

        log("my ID = $myId")
        log("recipient ID = $recipientId")
        log(recipientId == null)

        when {
            recipientId != 0L && recipientId == myId -> handleNotify(notify.content)
            recipientId == 0L && recipientId != myId -> authManager.sendPushToken()
            recipientId != 0L && recipientId != myId -> authManager.sendPushToken()
            recipientId == null -> handleNotify(notify.content)
        }
    }

    override fun onNewToken(token: String) {
        authManager.sendPushToken(token)
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun handleNotify(content: String) {
        val intent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.postDetailsFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("NMedia")
            .setContentText(content)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }
}