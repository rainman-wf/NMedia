package ru.netology.nmedia.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.application.App
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.presentation.activities.MainActivity
import ru.netology.nmedia.services.dto.Like
import ru.netology.nmedia.services.dto.NewPostInfo
import ru.netology.nmedia.services.dto.toPost
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onMessageReceived(message: RemoteMessage) {
        log(message.data)

        message.data[action]?.let {
            when (Action.getValidAction(it)) {
                Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                Action.NEW_POST ->
                    handleNewPost(gson.fromJson(message.data[content], NewPostInfo::class.java))
                Action.ERROR -> log(it) // send log to server
            }
        }
    }


    override fun onNewToken(token: String) {
        log(token)
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleLike(content: Like) {

        val intent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.postDetailsFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("User ${content.userName} liked ${content.postAuthor}'s post")
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(getDrawable(R.drawable.ic_notification)?.toBitmap())
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)

//        (application as App).appContainer.likePostUseCase.invoke(content.postId)
    }

    private fun handleNewPost(content: NewPostInfo) {

        val intent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.postDetailsFragment)
            .setArguments(bundleOf("postId" to content.id))
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("${content.author} опубликовал новый пост:")
            .setContentText(content.content.substring(0, 100) + "...")
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(getBitmapFromURL(content.authorAvatar))
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(getBitmapFromURL(content.thumbnail_url))
            )
            .setAutoCancel(true)

        val notId = Random.nextInt(100_000)

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(notId), notification.build())

//        (application as App).appContainer.addIncomingPostUseCase.invoke(content.toPost())
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            log("error stub")
            null
        }
    }
}