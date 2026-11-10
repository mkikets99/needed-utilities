package com.github.mkikets99.neededutility.tools.fuel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelScreen(onBack: () -> Unit) {
    var distanceInput by remember { mutableStateOf("") }
    var consumptionInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }

    val distance = distanceInput.toDoubleOrNull()
    val consumption = consumptionInput.toDoubleOrNull()
    val price = priceInput.toDoubleOrNull()
    val result = if (distance != null && consumption != null && price != null) {
        FuelCalculator.calculate(distance, consumption, price)
    } else null

    Scaffold(
        topBar = {
            ToolTopBar(title = "Fuel Calculator", onBack = onBack)
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
                value = distanceInput,
                onValueChange = { distanceInput = it },
                label = { Text("Distance (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = consumptionInput,
                onValueChange = { consumptionInput = it },
                label = { Text("Consumption (L/100km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it },
                label = { Text("Fuel price per liter") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRow("Fuel needed", result?.litersUsed, " L")
                    ResultRow("Trip cost", result?.totalCost, "")
                    ResultRow("Cost per km", result?.costPerKm, "")
                    ResultRow("Efficiency", result?.kmPerLiter, " km/L")
                }
            }
        }
    }
}


