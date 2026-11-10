package com.github.mkikets99.neededutility.tools.compass

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

internal fun cardinalDirection(degrees: Float): String {
    val labels = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((degrees + 22.5f) / 45f).toInt() % 8
    return labels[index]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompassScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val rotationSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }

    var azimuth by remember { mutableFloatStateOf(0f) }
    var hasSensor by remember { mutableStateOf(true) }

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val rotationMatrix = FloatArray(9)
                val orientation = FloatArray(3)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientation)
                azimuth = ((Math.toDegrees(orientation[0].toDouble()).toFloat()) + 360f) % 360f
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        val sensor = rotationSensor
        if (sensor == null) {
            hasSensor = false
        } else {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Compass", onBack = onBack)
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
                    text = "This device has no rotation vector sensor — compass unavailable.",
                    color = MaterialTheme.colorScheme.error,
                )
                return@Column
            }

            val primary = MaterialTheme.colorScheme.primary
            val onSurface = MaterialTheme.colorScheme.onSurfaceVariant
            val error = MaterialTheme.colorScheme.error

            Canvas(modifier = Modifier.size(260.dp)) {
                val radius = size.minDimension / 2f
                drawCircle(color = onSurface.copy(alpha = 0.15f), radius = radius)

                // Tick marks every 30 degrees (rotating dial).
                rotate(degrees = -azimuth) {
                    for (deg in 0 until 360 step 30) {
                        rotate(degrees = deg.toFloat()) {
                            val isCardinal = deg % 90 == 0
                            drawLine(
                                color = if (isCardinal) primary else onSurface.copy(alpha = 0.5f),
                                start = Offset(center.x, center.y - radius),
                                end = Offset(center.x, center.y - radius * (if (isCardinal) 0.82f else 0.9f)),
                                strokeWidth = if (isCardinal) 8f else 4f,
                            )
                        }
                    }
                    // North needle inside the rotating dial.
                    drawLine(
                        color = error,
                        start = Offset(center.x, center.y),
                        end = Offset(center.x, center.y - radius * 0.7f),
                        strokeWidth = 10f,
                    )
                }

                // Fixed top marker.
                drawLine(
                    color = primary,
                    start = Offset(center.x, center.y - radius),
                    end = Offset(center.x, center.y - radius * 1.08f),
                    strokeWidth = 12f,
                )
            }

            Text(
                text = "${azimuth.roundToInt()}° ${cardinalDirection(azimuth)}",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Red needle points north. Keep the phone flat, away from metal.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
