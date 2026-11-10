package com.github.mkikets99.neededutility.tools.geometry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.ui.common.ResultRow

private enum class Shape(val label: String) {
    CIRCLE("Circle"),
    RECTANGLE("Rectangle"),
    TRIANGLE("Triangle"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeometryScreen(onBack: () -> Unit) {
    var shape by remember { mutableStateOf(Shape.CIRCLE) }
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    var third by remember { mutableStateOf("") }

    val a = first.toDoubleOrNull()
    val b = second.toDoubleOrNull()
    val c = third.toDoubleOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geometry") },
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Shape.entries.forEach { entry ->
                    FilterChip(
                        selected = shape == entry,
                        onClick = {
                            shape = entry
                            first = ""
                            second = ""
                            third = ""
                        },
                        label = { Text(entry.label) },
                    )
                }
            }

            when (shape) {
                Shape.CIRCLE -> {
                    NumberField("Radius", first) { first = it }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            ResultRow("Area", a?.let { GeometryCalculator.circleArea(it) })
                            ResultRow(
                                "Circumference",
                                a?.let { GeometryCalculator.circleCircumference(it) },
                            )
                        }
                    }
                }

                Shape.RECTANGLE -> {
                    NumberField("Width", first) { first = it }
                    NumberField("Height", second) { second = it }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            ResultRow(
                                "Area",
                                if (a != null && b != null) GeometryCalculator.rectangleArea(a, b) else null,
                            )
                            ResultRow(
                                "Perimeter",
                                if (a != null && b != null) GeometryCalculator.rectanglePerimeter(a, b) else null,
                            )
                        }
                    }
                }

                Shape.TRIANGLE -> {
                    NumberField("Side a", first) { first = it }
                    NumberField("Side b", second) { second = it }
                    NumberField("Side c", third) { third = it }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val area =
                                if (a != null && b != null && c != null) {
                                    GeometryCalculator.triangleArea(a, b, c)
                                } else null
                            ResultRow("Area (Heron)", area)
                            ResultRow(
                                "Perimeter",
                                if (a != null && b != null && c != null) {
                                    GeometryCalculator.trianglePerimeter(a, b, c)
                                } else null,
                            )
                            if (a != null && b != null && c != null && area == null) {
                                Text(
                                    text = "These sides cannot form a triangle.",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}
