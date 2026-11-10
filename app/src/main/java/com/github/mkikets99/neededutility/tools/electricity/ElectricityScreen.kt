package com.github.mkikets99.neededutility.tools.electricity

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
fun ElectricityScreen(onBack: () -> Unit) {
    var wattsInput by remember { mutableStateOf("") }
    var hoursInput by remember { mutableStateOf("") }
    var daysInput by remember { mutableStateOf("30") }
    var priceInput by remember { mutableStateOf("") }

    val watts = wattsInput.toDoubleOrNull()
    val hours = hoursInput.toDoubleOrNull()
    val days = daysInput.toIntOrNull()
    val price = priceInput.toDoubleOrNull()
    val result = if (watts != null && hours != null && days != null && price != null) {
        ElectricityCalculator.calculate(watts, hours, days, price)
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Electricity Cost") },
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
                value = wattsInput,
                onValueChange = { wattsInput = it },
                label = { Text("Appliance power (W)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = hoursInput,
                onValueChange = { hoursInput = it },
                label = { Text("Hours per day (0–24)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = daysInput,
                onValueChange = { daysInput = it.filter(Char::isDigit).take(4) },
                label = { Text("Days") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it },
                label = { Text("Price per kWh") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRow("Per day", result?.kwhPerDay, " kWh")
                    ResultRow("Total energy", result?.kwhTotal, " kWh")
                    ResultRow("Cost per day", result?.costPerDay, "")
                    ResultRow("Total cost", result?.totalCost, "")
                }
            }
        }
    }
}


