package com.github.mkikets99.neededutility.tools.metronome

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen(onBack: () -> Unit) {
    var bpm by remember { mutableFloatStateOf(120f) }
    var running by remember { mutableStateOf(false) }
    var beat by remember { mutableIntStateOf(0) }

    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 80) }
    DisposableEffect(Unit) {
        onDispose { toneGenerator.release() }
    }

    LaunchedEffect(running) {
        while (running) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
            beat++
            delay(60_000L / bpm.toInt())
        }
    }

    val pulse by animateFloatAsState(
        targetValue = if (beat % 2 == 0) 1f else 1.25f,
        label = "beatPulse",
    )

    Scaffold(
        topBar = {
            ToolTopBar(title = "Metronome", onBack = onBack)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(if (running) pulse else 1f)
                    .background(
                        color = if (running) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = bpm.toInt().toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = if (running) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text("Beats per minute", style = MaterialTheme.typography.titleSmall)
            Slider(
                value = bpm,
                onValueChange = { bpm = it },
                valueRange = 30f..240f,
                steps = 209,
                enabled = !running,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = { running = !running },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
                Text(if (running) " Stop" else " Start")
            }

            Text(
                text = "Adjust the tempo before starting. Sound plays through the media stream.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
