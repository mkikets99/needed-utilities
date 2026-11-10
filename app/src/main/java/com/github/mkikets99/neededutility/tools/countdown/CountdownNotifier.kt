package com.github.mkikets99.neededutility.tools.countdown

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * Schedules local notifications for countdown events via AlarmManager.
 * Events fire at 09:00 local time on their date.
 */
object CountdownNotifier {

    const val CHANNEL_ID = "countdown"
    const val CHANNEL_NAME = "Countdown alerts"

    fun schedule(context: Context, event: CountdownEvent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAt = event.date.atTime(9, 0)
            .atZone(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val pendingIntent = pendingIntent(context, event.id, event.name)
        // S-02: exact alarms where permitted (Android 12+ grants them
        // optionally); graceful inexact fallback otherwise.
        val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            alarmManager.canScheduleExactAlarms()
        if (canExact) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent,
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent,
            )
        }
    }

    fun cancel(context: Context, eventId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context, eventId, ""))
    }

    private fun pendingIntent(context: Context, eventId: Long, name: String): PendingIntent {
        val intent = Intent(context, CountdownReceiver::class.java).apply {
            putExtra(EXTRA_EVENT_ID, eventId)
            putExtra(EXTRA_EVENT_NAME, name)
        }
        return PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    const val EXTRA_EVENT_ID = "event_id"
    const val EXTRA_EVENT_NAME = "event_name"
}
