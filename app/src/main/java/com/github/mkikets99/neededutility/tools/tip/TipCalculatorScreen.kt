package com.github.mkikets99.neededutility.tools.tip

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.data.ToolPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorScreen(
    onBack: () -> Unit,
    preferences: ToolPreferences,
) {
    val scope = rememberCoroutineScope()

    var billInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(15f) }
    var splitCount by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        preferences.tipPercent.collect { tipPercent = it.toFloat() }
    }
    LaunchedEffect(Unit) {
        preferences.splitCount.collect { splitCount = it }
    }

    val bill = billInput.toDoubleOrNull() ?: 0.0
    val tip = bill * tipPercent / 100.0
    val total = bill + tip
    val perPerson = if (splitCount > 0) total / splitCount else total

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tip & Split") },
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
            OutlinedTextField(
                value = billInput,
                onValueChange = { billInput = it },
                label = { Text("Bill amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Tip: ${tipPercent.toInt()}%", style = MaterialTheme.typography.titleSmall)
            Slider(
                value = tipPercent,
                onValueChange = { tipPercent = it },
                onValueChangeFinished = {
                    scope.launch { preferences.setTipPercent(tipPercent.toInt()) }
                },
                valueRange = 0f..30f,
                steps = 29,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(10, 15, 18, 20).forEach { preset ->
                    FilterChip(
                        selected = tipPercent.toInt() == preset,
                        onClick = {
                            tipPercent = preset.toFloat()
                            scope.launch { preferences.setTipPercent(preset) }
                        },
                        label = { Text("$preset%") },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Split between", style = MaterialTheme.typography.titleSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (splitCount > 1) {
                            splitCount--
                            scope.launch { preferences.setSplitCount(splitCount) }
                        }
                    }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Fewer people")
                    }
                    Text("$splitCount", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = {
                        if (splitCount < 99) {
                            splitCount++
                            scope.launch { preferences.setSplitCount(splitCount) }
                        }
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "More people")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRow("Tip", tip)
                    ResultRow("Total", total)
                    if (splitCount > 1) {
                        ResultRow("Per person", perPerson)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = String.format("%.2f", value),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
