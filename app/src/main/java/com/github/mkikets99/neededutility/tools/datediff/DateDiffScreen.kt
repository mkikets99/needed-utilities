package com.github.mkikets99.neededutility.tools.datediff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDiffScreen(onBack: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    var from by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var to by remember { mutableStateOf(LocalDate.now()) }
    var picker by remember { mutableStateOf<LocalDate?>(null) }

    val result = DateDiffLogic.between(from, to)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Date Diff") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { picker = from },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("From: ${from.format(formatter)}")
                }
                OutlinedButton(
                    onClick = { picker = to },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("To: ${to.format(formatter)}")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("Difference", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "${result.totalDays} days",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "${result.years} years, ${result.months} months, ${result.days} days",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = "${result.totalDays / 7} weeks and ${result.totalDays % 7} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

    picker?.let { picked ->
        val state = rememberDatePickerState(
            initialSelectedDateMillis = picked.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { picker = null },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        if (picked == from) from = selected else to = selected
                    }
                    picker = null
                }) {
                    Text("OK")
                }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
