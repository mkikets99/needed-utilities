package com.github.mkikets99.neededutility.tools.fractions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.ui.common.ResultRow
import com.github.mkikets99.neededutility.ui.common.ResultRowText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FractionScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val fraction = Fractions.parse(input)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fractions") },
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
                value = input,
                onValueChange = { input = it },
                label = { Text("Fraction (3/4) or decimal (0.75)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("Result", style = MaterialTheme.typography.labelMedium)
                    ResultRowText(
                        label = "Simplified fraction",
                        value = fraction?.let { "${it.numerator} / ${it.denominator}" } ?: "—",
                    )
                    ResultRow("Decimal", fraction?.decimal, pattern = "%.6f")
                }
            }

            if (input.isNotBlank() && fraction == null) {
                Text(
                    text = "Enter a fraction like 3/4 or a decimal like 0.75.",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
