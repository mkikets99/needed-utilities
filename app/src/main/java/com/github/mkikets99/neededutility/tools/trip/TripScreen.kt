package com.github.mkikets99.neededutility.tools.trip

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
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mkikets99.neededutility.monetization.Limits
import com.github.mkikets99.neededutility.monetization.PaywallDialog
import com.github.mkikets99.neededutility.monetization.ProState
import com.github.mkikets99.neededutility.ui.common.ResultRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripExpensesRepository(context.applicationContext) }
    val expenses by repository.expenses.collectAsStateWithLifecycle(initialValue = emptyList())

    var people by remember { mutableIntStateOf(2) }
    var showDialog by remember { mutableStateOf(false) }
    var showPaywall by remember { mutableStateOf(false) }

    val isPro by ProState.isPro.collectAsStateWithLifecycle()

    val total = TripSplit.total(expenses)
    val perPerson = TripSplit.perPerson(total, people)

    Scaffold(
        topBar = {
            ToolTopBar(title = "Trip & Expenses", onBack = onBack)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (Limits.canAdd(expenses.size, isPro, Limits.MAX_TRIP_ENTRIES)) {
                    showDialog = true
                } else {
                    showPaywall = true
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add expense")
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
                    ResultRow("Total", total)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Split between", style = MaterialTheme.typography.bodyLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (people > 1) people-- }) {
                                Icon(Icons.Filled.Remove, contentDescription = "Fewer people")
                            }
                            Text("$people", style = MaterialTheme.typography.titleLarge)
                            IconButton(onClick = { if (people < 99) people++ }) {
                                Icon(Icons.Filled.Add, contentDescription = "More people")
                            }
                        }
                    }
                    ResultRow("Per person", perPerson)
                }
            }

            if (expenses.isEmpty()) {
                Text(
                    text = "No expenses yet. Tap + to add one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                ) {
                    items(expenses, key = { it.id }) { expense ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(expense.label, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        text = String.format("%.2f", expense.amount),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                IconButton(onClick = {
                                    scope.launch { repository.delete(expense.id) }
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete expense")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onSave = { label, amount ->
                scope.launch { repository.add(label, amount) }
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }

    if (showPaywall) {
        PaywallDialog(onDismiss = { showPaywall = false })
    }
}

@Composable
private fun AddExpenseDialog(
    onSave: (String, Double) -> Unit,
    onDismiss: () -> Unit,
) {
    var label by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountInput.toDoubleOrNull()
                    if (label.isNotBlank() && amount != null && amount > 0) {
                        onSave(label, amount)
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
