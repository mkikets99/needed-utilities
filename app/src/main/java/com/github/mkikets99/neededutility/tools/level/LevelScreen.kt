package com.github.mkikets99.neededutility.tools.level

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    var pitch by remember { mutableFloatStateOf(0f) }
    var roll by remember { mutableFloatStateOf(0f) }
    var hasSensor by remember { mutableStateOf(true) }
    var zeroPitch by remember { mutableFloatStateOf(0f) }
    var zeroRoll by remember { mutableFloatStateOf(0f) }

    // S-04: "Zero here" calibration against the current surface.
    val adjPitch = pitch - zeroPitch
    val adjRoll = roll - zeroRoll

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                pitch = Math.toDegrees(
                    atan2(-x.toDouble(), sqrt((y * y + z * z).toDouble()))
                ).toFloat()
                roll = Math.toDegrees(atan2(y.toDouble(), z.toDouble())).toFloat()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        val sensor = accelerometer
        if (sensor == null) {
            hasSensor = false
        } else {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    val isLevel = kotlin.math.abs(adjPitch) < 1f && kotlin.math.abs(adjRoll) < 1f

    Scaffold(
        topBar = {
            ToolTopBar(title = "Bubble Level", onBack = onBack)
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
            if (!hasSensor) {
                Text(
                    text = "This device has no accelerometer — level unavailable.",
                    color = MaterialTheme.colorScheme.error,
                )
                return@Column
            }

            Box(
                modifier = Modifier.size(260.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Surface
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = CircleShape,
                        ),
                )
                // Crosshair lines via two thin boxes
                Box(
                    modifier = Modifier
                        .size(width = 260.dp, height = 2.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                )
                Box(
                    modifier = Modifier
                        .size(width = 2.dp, height = 260.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                )
                // Center target
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            shape = CircleShape,
                        ),
                )
                // Bubble
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            translationX = (adjRoll / 45f).coerceIn(-1f, 1f) * 90.dp.toPx()
                            translationY = (adjPitch / 45f).coerceIn(-1f, 1f) * 90.dp.toPx()
                        }
                        .background(
                            color = if (isLevel) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape,
                        ),
                )
            }

            Text(
                text = if (isLevel) "Level!" else "Tilted",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isLevel) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(
                    text = "Pitch ${adjPitch.roundToInt()}°",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Roll ${adjRoll.roundToInt()}°",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        zeroPitch = pitch
                        zeroRoll = roll
                    },
                ) {
                    Text("Zero here")
                }
                OutlinedButton(
                    onClick = {
                        zeroPitch = 0f
                        zeroRoll = 0f
                    },
                ) {
                    Text("Reset")
                }
            }
            Text(
                text = "Place the phone on a surface; the bubble settles in the center when level.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
