package com.github.mkikets99.neededutility.tools.countdown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Re-schedules countdown alarms after a device reboot (alarms do not survive
 * reboot on Android).
 */
class CountdownBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CountdownEventsRepository(context.applicationContext)
                val today = LocalDate.now()
                repository.events.first()
                    .filter { !it.date.isBefore(today) }
                    .forEach { CountdownNotifier.schedule(context, it) }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
