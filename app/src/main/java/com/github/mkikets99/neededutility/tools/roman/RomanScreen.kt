package com.github.mkikets99.neededutility.tools.roman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanScreen(onBack: () -> Unit) {
    var numberInput by remember { mutableStateOf("") }
    var romanInput by remember { mutableStateOf("") }
    var numberError by remember { mutableStateOf(false) }
    var romanError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roman Numerals") },
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
                value = numberInput,
                onValueChange = {
                    numberInput = it.filter(Char::isDigit).take(4)
                    numberError = false
                    romanError = false
                    val parsed = numberInput.toIntOrNull()
                    when {
                        numberInput.isEmpty() -> romanInput = ""
                        parsed == null -> numberError = true
                        else -> {
                            val roman = RomanNumerals.toRoman(parsed)
                            if (roman == null) numberError = true else romanInput = roman
                        }
                    }
                },
                label = { Text("Number (1–3999)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                isError = numberError,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = romanInput,
                onValueChange = {
                    romanInput = it.uppercase().filter { ch -> ch in "IVXLCDM" }.take(15)
                    numberError = false
                    romanError = false
                    when {
                        romanInput.isEmpty() -> numberInput = ""
                        else -> {
                            val value = RomanNumerals.fromRoman(romanInput)
                            if (value == null) romanError = true else numberInput = value.toString()
                        }
                    }
                },
                label = { Text("Roman (I, V, X, L, C, D, M)") },
                singleLine = true,
                isError = romanError,
                modifier = Modifier.fillMaxWidth(),
            )

            if (numberError || romanError) {
                Text(
                    text = "Not a valid value (supported range: 1–3999).",
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Text(
                text = "Type in either field — the other updates live.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
