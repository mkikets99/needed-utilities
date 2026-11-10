package com.github.mkikets99.neededutility.tools.salary

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

private enum class SalaryMode(val label: String) {
    HOURLY("Hourly"),
    MONTHLY("Monthly"),
    ANNUAL("Annual"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf(SalaryMode.HOURLY) }
    var valueInput by remember { mutableStateOf("") }
    var hoursInput by remember { mutableStateOf("40") }
    var weeksInput by remember { mutableStateOf("52") }

    val value = valueInput.toDoubleOrNull()
    val hours = hoursInput.toDoubleOrNull()
    val weeks = weeksInput.toDoubleOrNull()
    val result = if (value != null && hours != null && weeks != null) {
        when (mode) {
            SalaryMode.HOURLY -> SalaryConverter.fromHourly(value, hours, weeks)
            SalaryMode.MONTHLY -> SalaryConverter.fromMonthly(value, hours, weeks)
            SalaryMode.ANNUAL -> SalaryConverter.fromAnnual(value, hours, weeks)
        }
    } else null

    Scaffold(
        topBar = {
            ToolTopBar(title = "Salary Converter", onBack = onBack)
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
                SalaryMode.entries.forEach { entry ->
                    FilterChip(
                        selected = mode == entry,
                        onClick = {
                            mode = entry
                            valueInput = ""
                        },
                        label = { Text(entry.label) },
                    )
                }
            }

            OutlinedTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = { Text("${mode.label} pay") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = hoursInput,
                    onValueChange = { hoursInput = it },
                    label = { Text("Hours / week") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = weeksInput,
                    onValueChange = { weeksInput = it },
                    label = { Text("Weeks / year") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRow("Hourly", result?.hourly)
                    ResultRow("Monthly", result?.monthly)
                    ResultRow("Annual", result?.annual)
                    Text(
                        text = "Gross pay — before taxes and deductions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
