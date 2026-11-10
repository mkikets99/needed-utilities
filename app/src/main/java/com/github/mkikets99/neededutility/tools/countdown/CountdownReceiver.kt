package com.github.mkikets99.neededutility.tools.countdown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.mkikets99.neededutility.ui.common.showCompletionNotification

class CountdownReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(CountdownNotifier.EXTRA_EVENT_ID, 0L)
        val name = intent.getStringExtra(CountdownNotifier.EXTRA_EVENT_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: "Countdown"
        showCompletionNotification(
            context = context,
            channelId = CountdownNotifier.CHANNEL_ID,
            channelName = CountdownNotifier.CHANNEL_NAME,
            title = "$name is today!",
            text = "Your countdown has arrived.",
            notificationId = id.toInt(),
        )
    }
}
