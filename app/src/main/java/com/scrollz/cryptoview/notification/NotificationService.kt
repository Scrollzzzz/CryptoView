package com.scrollz.cryptoview.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.scrollz.cryptoview.R
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.utils.toPriceFormat

class NotificationService(
    private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(coin: Coin) {
        val launchIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://com.scrollz.cryptoview/coin/${coin.id}")
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, EXCHANGE_RATE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(coin.symbol)
            .setContentText("${coin.name} price is: ${coin.price.toPriceFormat()}")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(coin.id.hashCode(), notification)
    }

    companion object {
        const val EXCHANGE_RATE_CHANNEL_ID = "exchange_rate_channel"
    }
}
