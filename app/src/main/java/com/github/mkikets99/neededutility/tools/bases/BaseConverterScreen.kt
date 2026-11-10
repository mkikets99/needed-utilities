package com.github.mkikets99.neededutility.tools.bases

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private fun radixLabel(radix: Int): String = when (radix) {
    2 -> "BIN"
    8 -> "OCT"
    10 -> "DEC"
    16 -> "HEX"
    else -> radix.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverterScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var inputRadix by remember { mutableIntStateOf(10) }

    val value = BaseConverter.parse(input, inputRadix)

    Scaffold(
        topBar = {
            ToolTopBar(title = "Base Converter", onBack = onBack)
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
                BaseConverter.SUPPORTED_RADICES.forEach { radix ->
                    FilterChip(
                        selected = inputRadix == radix,
                        onClick = { inputRadix = radix },
                        label = { Text(radixLabel(radix)) },
                    )
                }
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Value (${radixLabel(inputRadix)})") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            if (input.isNotBlank() && value == null) {
                Text(
                    text = "Not a valid ${radixLabel(inputRadix)} number.",
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    BaseConverter.SUPPORTED_RADICES.forEach { radix ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                radixLabel(radix),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = value?.let { BaseConverter.format(it, radix) } ?: "—",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (radix == inputRadix) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
