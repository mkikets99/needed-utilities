package com.github.mkikets99.neededutility.ui.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.github.mkikets99.neededutility.R

/**
 * Shared completion feedback used by timer-like tools (timer, multi timer,
 * countdown notifications).
 */

@Suppress("DEPRECATION")
fun vibrateDevice(context: Context, durationMs: Long = 600) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
}

fun showCompletionNotification(
    context: Context,
    channelId: String,
    channelName: String,
    title: String,
    text: String,
    notificationId: Int,
) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
    )
    val notification = Notification.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_stat_timer)
        .setContentTitle(title)
        .setContentText(text)
        .setAutoCancel(true)
        .build()
    manager.notify(notificationId, notification)
}
