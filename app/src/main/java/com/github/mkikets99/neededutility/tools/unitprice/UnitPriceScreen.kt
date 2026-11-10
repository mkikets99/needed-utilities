package com.github.mkikets99.neededutility.tools.unitprice

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
import com.github.mkikets99.neededutility.ui.common.ResultRowText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitPriceScreen(onBack: () -> Unit) {
    var priceA by remember { mutableStateOf("") }
    var qtyA by remember { mutableStateOf("") }
    var priceB by remember { mutableStateOf("") }
    var qtyB by remember { mutableStateOf("") }

    val packA = Pack(priceA.toDoubleOrNull() ?: -1.0, qtyA.toDoubleOrNull() ?: 0.0)
    val packB = Pack(priceB.toDoubleOrNull() ?: -1.0, qtyB.toDoubleOrNull() ?: 0.0)
    val result = UnitPriceCalculator.compare(packA, packB)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Price") },
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
            PackCard("Pack A", priceA, { priceA = it }, qtyA, { qtyA = it })
            PackCard("Pack B", priceB, { priceB = it }, qtyB, { qtyB = it })

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRowText("Unit price A", formatUnitPrice(result?.unitPriceA))
                    ResultRowText("Unit price B", formatUnitPrice(result?.unitPriceB))
                    Text(
                        text = when {
                            result == null -> "Enter price and quantity for both packs."
                            result.cheaperIsA == null -> "Both packs cost the same per unit."
                            else -> {
                                val winner = if (result.cheaperIsA) "Pack A" else "Pack B"
                                "$winner is cheaper by ${String.format("%.1f", result.savingsPercent)}%"
                            }
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun PackCard(
    title: String,
    price: String,
    onPriceChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                label = { Text("Quantity / weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun formatUnitPrice(value: Double?): String =
    value?.let { String.format("%.4f", it).trimEnd('0').trimEnd('.') } ?: "—"
