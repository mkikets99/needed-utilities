package com.github.mkikets99.neededutility.tools.savings

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
fun SavingsScreen(onBack: () -> Unit) {
    var principalInput by remember { mutableStateOf("") }
    var rateInput by remember { mutableStateOf("") }
    var yearsInput by remember { mutableStateOf("") }
    var depositInput by remember { mutableStateOf("") }

    val principal = principalInput.toDoubleOrNull()
    val rate = rateInput.toDoubleOrNull()
    val years = yearsInput.toIntOrNull()
    val deposit = depositInput.toDoubleOrNull()
    val result = if (principal != null && rate != null && years != null && deposit != null) {
        SavingsCalculator.calculate(principal, rate, years, deposit)
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Savings & Interest") },
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
                value = principalInput,
                onValueChange = { principalInput = it },
                label = { Text("Starting amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = rateInput,
                onValueChange = { rateInput = it },
                label = { Text("Annual interest (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = yearsInput,
                onValueChange = { yearsInput = it.filter(Char::isDigit).take(3) },
                label = { Text("Years") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = depositInput,
                onValueChange = { depositInput = it },
                label = { Text("Monthly deposit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ResultRow("Future value", result?.futureValue)
                    ResultRow("Total contributed", result?.totalContributed)
                    ResultRow("Interest earned", result?.interestEarned)
                    Text(
                        text = "Compounded monthly; deposits added at the end of each month.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}


