package com.scrollz.cryptoview.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.scrollz.cryptoview.domain.model.Notification
import java.util.Calendar
import java.util.Locale

class AlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(notification: Notification) {
        val timeToAlarm = Calendar.getInstance(Locale.getDefault())
        timeToAlarm.timeInMillis = System.currentTimeMillis()
        timeToAlarm.set(Calendar.HOUR_OF_DAY, notification.hour)
        timeToAlarm.set(Calendar.MINUTE, notification.minute)
        timeToAlarm.set(Calendar.SECOND, 0)
        timeToAlarm.set(Calendar.MILLISECOND, 0)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("coinID", notification.coinID)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeToAlarm.timeInMillis,
            (1000 * 60 * 10).toLong(),
            PendingIntent.getBroadcast(
                context,
                notification.coinID.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancelAlarm(id: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
