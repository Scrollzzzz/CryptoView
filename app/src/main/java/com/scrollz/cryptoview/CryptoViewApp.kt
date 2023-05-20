package com.scrollz.cryptoview

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.scrollz.cryptoview.notification.NotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CryptoViewApp: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationService.EXCHANGE_RATE_CHANNEL_ID,
            getString(R.string.notification_channel_exchange_rates),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
