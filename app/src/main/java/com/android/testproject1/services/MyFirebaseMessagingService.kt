package com.android.testproject1.services

import android.app.NotificationManager
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.testproject1.R
import com.android.testproject1.services.App.Companion.FCM_CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("MyTag","onMessageReceivedCalled")
        Log.d("MyTag","Message received from : "+ p0.from)

        if (p0.notification!=null){
            val title= p0.notification!!.title
            val body= p0.notification!!.body
            val imageUrl= p0.notification!!.imageUrl

            val notifications=NotificationCompat.Builder(this,FCM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setContentTitle(title)
                .setContentText(body)
//                .setLargeIcon(convertToBitmap(imageUrl.toString()))
                .build()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1002,notifications)
        }
    }

    private fun convertToBitmap(urlImage:String):Bitmap{

//        try {
            val url = URL(urlImage)
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        } catch (e: IOException) {
//        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("MyTag","onNewToken")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.d("MyTag","onDeletedMessages")
    }
}