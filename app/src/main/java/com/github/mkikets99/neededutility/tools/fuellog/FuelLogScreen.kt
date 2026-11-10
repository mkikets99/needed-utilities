package com.github.mkikets99.neededutility.tools.fuellog

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mkikets99.neededutility.ui.common.ResultRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelLogScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FuelLogRepository(context.applicationContext) }
    val fillUps by repository.fillUps.collectAsStateWithLifecycle(initialValue = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    val stats = FuelStats.compute(fillUps)

    Scaffold(
        topBar = {
            ToolTopBar(title = "Fuel Log", onBack = onBack)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add fill-up")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("Statistics", style = MaterialTheme.typography.labelMedium)
                    ResultRow(
                        "Average consumption",
                        stats?.avgConsumptionL100,
                        " L/100km",
                    )
                    ResultRow("Average price", stats?.avgPricePerLiter, " / L")
                    ResultRow("Total fuel", stats?.totalLiters, " L")
                    ResultRow("Total cost", stats?.totalCost)
                    ResultRow("Distance", stats?.totalDistance, " km")
                    if (fillUps.size < 2) {
                        Text(
                            text = "Add at least two fill-ups to see consumption.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (fillUps.isEmpty()) {
                Text(
                    text = "No fill-ups yet. Tap + to add one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                ) {
                    items(fillUps.sortedByDescending { it.odometerKm }, key = { it.id }) { fillUp ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${String.format("%.1f", fillUp.odometerKm)} km",
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        "${String.format("%.2f", fillUp.liters)} L × " +
                                            "${String.format("%.2f", fillUp.pricePerLiter)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                IconButton(onClick = {
                                    scope.launch { repository.delete(fillUp.id) }
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete fill-up")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddFillUpDialog(
            onSave = { odometer, liters, price ->
                scope.launch { repository.add(odometer, liters, price) }
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
private fun AddFillUpDialog(
    onSave: (Double, Double, Double) -> Unit,
    onDismiss: () -> Unit,
) {
    var odometerInput by remember { mutableStateOf("") }
    var litersInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add fill-up") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = odometerInput,
                    onValueChange = { odometerInput = it },
                    label = { Text("Odometer (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = litersInput,
                    onValueChange = { litersInput = it },
                    label = { Text("Liters") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = priceInput,
                    onValueChange = { priceInput = it },
                    label = { Text("Price per liter") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val odometer = odometerInput.toDoubleOrNull()
                    val liters = litersInput.toDoubleOrNull()
                    val price = priceInput.toDoubleOrNull()
                    if (odometer != null && liters != null && price != null &&
                        odometer > 0 && liters > 0 && price >= 0
                    ) {
                        onSave(odometer, liters, price)
                    }
                },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
