package com.github.mkikets99.neededutility.tools.sunrise

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.ui.common.ResultRowText
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunriseScreen(onBack: () -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    var date by remember { mutableStateOf(LocalDate.now()) }
    var latInput by remember { mutableStateOf("50.45") }
    var lonInput by remember { mutableStateOf("30.52") }
    var showPicker by remember { mutableStateOf(false) }

    val lat = latInput.toDoubleOrNull()
    val lon = lonInput.toDoubleOrNull()
    val result = if (lat != null && lon != null) {
        SunriseCalculator.calculate(date, lat, lon)
    } else null

    Scaffold(
        topBar = {
            ToolTopBar(title = "Sunrise & Sunset", onBack = onBack)
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
            OutlinedButton(
                onClick = { showPicker = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Date: ${date.format(dateFormatter)}")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = latInput,
                    onValueChange = { latInput = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = lonInput,
                    onValueChange = { lonInput = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    when {
                        result == null -> Text(
                            text = "Enter valid coordinates (lat −90…90, lon −180…180).",
                            color = MaterialTheme.colorScheme.error,
                        )

                        result.polarState == PolarState.POLAR_DAY -> {
                            Text("Midnight sun — the sun does not set.", style = MaterialTheme.typography.titleSmall)
                            ResultRowText("Solar noon", timeFormatter.format(result.solarNoonUtc))
                        }

                        result.polarState == PolarState.POLAR_NIGHT -> {
                            Text("Polar night — the sun does not rise.", style = MaterialTheme.typography.titleSmall)
                            ResultRowText("Solar noon", timeFormatter.format(result.solarNoonUtc))
                        }

                        else -> {
                            ResultRowText("Sunrise", timeFormatter.format(result.sunriseUtc!!))
                            ResultRowText("Sunset", timeFormatter.format(result.sunsetUtc!!))
                            ResultRowText("Solar noon", timeFormatter.format(result.solarNoonUtc))
                        }
                    }
                }
            }

            Text(
                text = "Times shown in your local timezone (UTC under the hood).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (showPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showPicker = false
                }) {
                    Text("OK")
                }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
