package com.android.testproject1.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


class App : Application() {

    companion object{
        val CHANNEL_ID = "exampleServiceChannel"
        val CHANNEL_ID2 = "exampleServiceChannel2"
        val FCM_CHANNEL_ID = "fcmChannel"

        private var appContext: Context? = null
        fun getAppContext(): Context? {
            return appContext
        }
    }


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        createNotificationChannel()
        createNotificationChannel2()
        createNotificationChannel3()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel2 = NotificationChannel(
                CHANNEL_ID2,
                "Example Service Channel2",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel2)
        }
    }

    private fun createNotificationChannel3() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val fcmChannel = NotificationChannel(
                FCM_CHANNEL_ID,
                "FCM_Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(fcmChannel)
        }
    }


}