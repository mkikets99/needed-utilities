package com.github.mkikets99.neededutility.tools.multitimer

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.ui.common.showCompletionNotification
import com.github.mkikets99.neededutility.ui.common.vibrateDevice
import kotlinx.coroutines.delay

private data class TimerSpec(val id: Long, val name: String, val totalMs: Long)

private val kitchenPresets = listOf(
    "Soft egg" to 7,
    "Tea" to 4,
    "Pasta" to 10,
    "Pizza" to 15,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTimerScreen(onBack: () -> Unit) {
    var timers by remember { mutableStateOf(listOf<TimerSpec>()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Multi Timer", onBack = onBack)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add timer")
            }
        },
    ) { innerPadding ->
        if (timers.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "No timers yet.\nTap + to add one (kitchen presets included).",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(timers, key = { it.id }) { spec ->
                    TimerRow(
                        spec = spec,
                        onDelete = { timers = timers.filterNot { it.id == spec.id } },
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddTimerDialog(
            onSave = { name, minutes ->
                timers = timers + TimerSpec(
                    id = System.currentTimeMillis(),
                    name = name,
                    totalMs = minutes * 60_000L,
                )
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
private fun TimerRow(spec: TimerSpec, onDelete: () -> Unit) {
    val context = LocalContext.current
    var remainingMs by remember(spec.id) { mutableLongStateOf(spec.totalMs) }
    var running by remember(spec.id) { mutableStateOf(false) }

    LaunchedEffect(running) {
        if (running) {
            var last = SystemClock.elapsedRealtime()
            while (remainingMs > 0L) {
                delay(50)
                val now = SystemClock.elapsedRealtime()
                remainingMs = (remainingMs - (now - last)).coerceAtLeast(0L)
                last = now
            }
            running = false
            if (remainingMs == 0L) {
                vibrateDevice(context)
                showCompletionNotification(
                    context = context,
                    channelId = "multi_timer",
                    channelName = "Multi Timer alerts",
                    title = "${spec.name} finished!",
                    text = "Your timer has ended.",
                    notificationId = spec.id.toInt(),
                )
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(spec.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = formatRemaining(remainingMs),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (remainingMs == 0L) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = { running = !running }, enabled = remainingMs > 0L) {
                Icon(
                    if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (running) "Pause" else "Start",
                )
            }
            IconButton(onClick = {
                running = false
                remainingMs = spec.totalMs
            }) {
                Icon(Icons.Filled.Replay, contentDescription = "Reset")
            }
            IconButton(onClick = {
                running = false
                onDelete()
            }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete timer")
            }
        }
    }
}

@Composable
private fun AddTimerDialog(
    onSave: (String, Long) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var minutesInput by remember { mutableStateOf("5") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New timer") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = minutesInput,
                    onValueChange = { minutesInput = it.filter(Char::isDigit).take(3) },
                    label = { Text("Minutes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text("Kitchen presets", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    kitchenPresets.forEach { (presetName, minutes) ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                onSave(presetName, minutes.toLong())
                            },
                            label = { Text("$presetName ${minutes}m") },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val minutes = minutesInput.toLongOrNull()
                    if (minutes != null && minutes > 0) {
                        onSave(name.ifBlank { "Timer" }, minutes)
                    }
                },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

internal fun formatRemaining(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
