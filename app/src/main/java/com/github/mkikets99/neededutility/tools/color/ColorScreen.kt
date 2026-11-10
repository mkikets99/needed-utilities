package com.github.mkikets99.neededutility.tools.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.ui.common.ResultRowText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorScreen(onBack: () -> Unit) {
    var red by remember { mutableFloatStateOf(0f) }
    var green by remember { mutableFloatStateOf(105f) }
    var blue by remember { mutableFloatStateOf(110f) }
    var hexInput by remember { mutableStateOf("#00696E") }

    fun syncFromSliders() {
        hexInput = ColorConverter.toHex(red.toInt(), green.toInt(), blue.toInt())
    }

    val color = Color(red.toInt(), green.toInt(), blue.toInt())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Color Converter") },
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
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = color, shape = RoundedCornerShape(16.dp)),
            )

            OutlinedTextField(
                value = hexInput,
                onValueChange = { input ->
                    hexInput = input
                    ColorConverter.fromHex(input)?.let { rgb ->
                        red = rgb.red.toFloat()
                        green = rgb.green.toFloat()
                        blue = rgb.blue.toFloat()
                    }
                },
                label = { Text("Hex (#RRGGBB)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            SliderRow("Red", red) {
                red = it
                syncFromSliders()
            }
            SliderRow("Green", green) {
                green = it
                syncFromSliders()
            }
            SliderRow("Blue", blue) {
                blue = it
                syncFromSliders()
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRowText("Hex", ColorConverter.toHex(red.toInt(), green.toInt(), blue.toInt()))
                    ResultRowText("RGB", "${red.toInt()}, ${green.toInt()}, ${blue.toInt()}")
                }
            }
        }
    }
}

@Composable
private fun SliderRow(label: String, value: Float, onChange: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, style = MaterialTheme.typography.titleSmall)
            Text(
                value.toInt().toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = 0f..255f,
        )
    }
}


