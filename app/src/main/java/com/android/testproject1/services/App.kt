package com.android.testproject1.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


class App : Application() {

    companion object{
        val CHANNEL_ID = "exampleServiceChannel"

        private var appContext: Context? = null
        fun getAppContext(): Context? {
            return appContext
        }
    }


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        createNotificationChannel()
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
}