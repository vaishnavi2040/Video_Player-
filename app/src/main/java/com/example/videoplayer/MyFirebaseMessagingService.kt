package com.example.videoplayer

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL



    class MyFirebaseMessagingService : FirebaseMessagingService() {
        var bitmap: Bitmap? = null
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            val message = remoteMessage.data["message"]
            //imageUri will contain URL of the image to be displayed with Notification
            val imageUri = remoteMessage.data["image"]
            val link = remoteMessage.data["link"]

            //To get a Bitmap image from the URL received
            bitmap = getBitmapfromUrl(imageUri)
            sendNotification(message, bitmap, link)
        }

        /**
         * Create and show a simple notification containing the received FCM message.
         */
        private fun sendNotification(messageBody: String?, image: Bitmap?, link: String?) {
            val intent = Intent(this, PlayerNewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("LINK", link)
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: Notification.Builder = Notification.Builder(this)
                .setLargeIcon(image)
                .setContentTitle(messageBody)
                .setSmallIcon(R.drawable.sym_def_app_icon)
                /*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

        fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
            return try {
                val url = URL(imageUrl)
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                null
            }
        }
    }
