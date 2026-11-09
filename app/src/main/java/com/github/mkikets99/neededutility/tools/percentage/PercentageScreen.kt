package com.github.mkikets99.neededutility.tools.percentage

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

private enum class PercentageMode(val label: String, val firstLabel: String, val secondLabel: String) {
    PERCENT_OF("P% of X", "Percent (P)", "Value (X)"),
    WHAT_PERCENT("X is ?% of Y", "Part (X)", "Whole (Y)"),
    CHANGE("% change X → Y", "From (X)", "To (Y)"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentageScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf(PercentageMode.PERCENT_OF) }
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }

    val a = first.toDoubleOrNull()
    val b = second.toDoubleOrNull()
    val result = if (a != null && b != null) {
        when (mode) {
            PercentageMode.PERCENT_OF -> PercentageLogic.percentOf(a, b)
            PercentageMode.WHAT_PERCENT -> PercentageLogic.whatPercentIs(a, b)
            PercentageMode.CHANGE -> PercentageLogic.percentChange(a, b)
        }
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Percentage") },
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
                PercentageMode.entries.forEach { entry ->
                    FilterChip(
                        selected = mode == entry,
                        onClick = {
                            mode = entry
                            first = ""
                            second = ""
                        },
                        label = { Text(entry.label) },
                    )
                }
            }

            OutlinedTextField(
                value = first,
                onValueChange = { first = it },
                label = { Text(mode.firstLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = second,
                onValueChange = { second = it },
                label = { Text(mode.secondLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = result?.let { String.format("%.4f", it).trimEnd('0').trimEnd('.') } ?: "—",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (result != null && (mode == PercentageMode.WHAT_PERCENT || mode == PercentageMode.CHANGE)) {
                        Text(
                            text = "%",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (result == null && first.isNotBlank() && second.isNotBlank()) {
                        Text(
                            text = if (mode == PercentageMode.PERCENT_OF) "Enter valid numbers."
                            else "The second value cannot be zero.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}
