package com.github.mkikets99.neededutility.tools.stopwatch

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stopwatch & Timer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Stopwatch") },
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Timer") },
                )
            }
            when (selectedTab) {
                0 -> StopwatchTab()
                else -> TimerTab()
            }
        }
    }
}

@Composable
private fun StopwatchTab() {
    var running by remember { mutableStateOf(false) }
    var baseMs by remember { mutableLongStateOf(0L) }
    var startedAtMs by remember { mutableLongStateOf(0L) }
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var laps by remember { mutableStateOf(listOf<Long>()) }

    LaunchedEffect(running) {
        if (running) {
            startedAtMs = SystemClock.elapsedRealtime()
            while (true) {
                elapsedMs = baseMs + (SystemClock.elapsedRealtime() - startedAtMs)
                delay(50)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = formatElapsed(elapsedMs),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    if (running) {
                        baseMs = elapsedMs
                        running = false
                    } else {
                        running = true
                    }
                },
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
                Text(if (running) " Pause" else " Start")
            }
            FilledTonalButton(
                onClick = {
                    running = false
                    baseMs = 0L
                    elapsedMs = 0L
                    laps = emptyList()
                },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Filled.Replay, contentDescription = null)
                Text(" Reset")
            }
            FilledTonalButton(
                onClick = { laps = listOf(elapsedMs) + laps },
                enabled = elapsedMs > 0L,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Filled.Flag, contentDescription = null)
                Text(" Lap")
            }
        }

        if (laps.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(laps.size) { index ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Lap ${laps.size - index}")
                        Text(formatElapsed(laps[index]))
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerTab() {
    var minutesInput by remember { mutableStateOf("1") }
    var secondsInput by remember { mutableStateOf("0") }
    var remainingMs by remember { mutableLongStateOf(60_000L) }
    var running by remember { mutableStateOf(false) }

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
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = formatElapsed(remainingMs),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = minutesInput,
                onValueChange = {
                    minutesInput = it.filter(Char::isDigit).take(2)
                    remainingMs = totalFromInputs(minutesInput, secondsInput)
                },
                label = { Text("Min") },
                singleLine = true,
                enabled = !running,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = secondsInput,
                onValueChange = {
                    secondsInput = it.filter(Char::isDigit).take(2)
                    remainingMs = totalFromInputs(minutesInput, secondsInput)
                },
                label = { Text("Sec") },
                singleLine = true,
                enabled = !running,
                modifier = Modifier.weight(1f),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { running = !running },
                enabled = remainingMs > 0L,
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
                Text(if (running) " Pause" else " Start")
            }
            FilledTonalButton(
                onClick = {
                    running = false
                    remainingMs = totalFromInputs(minutesInput, secondsInput)
                },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Filled.Replay, contentDescription = null)
                Text(" Reset")
            }
        }
    }
}

private fun totalFromInputs(minutes: String, seconds: String): Long {
    val m = minutes.toLongOrNull() ?: 0L
    val s = (seconds.toLongOrNull() ?: 0L).coerceIn(0L, 59L)
    return (m * 60L + s) * 1000L
}

private fun formatElapsed(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val centis = (ms % 1000) / 10
    return if (hours > 0) {
        String.format("%d:%02d:%02d.%02d", hours, minutes, seconds, centis)
    } else {
        String.format("%02d:%02d.%02d", minutes, seconds, centis)
    }
}
