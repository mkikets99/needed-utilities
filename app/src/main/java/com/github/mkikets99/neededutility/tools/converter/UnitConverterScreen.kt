package com.github.mkikets99.neededutility.tools.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.data.ToolPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    onBack: () -> Unit,
    preferences: ToolPreferences,
) {
    val scope = rememberCoroutineScope()

    var category by remember { mutableStateOf(ConvertCategory.LENGTH) }
    var fromUnit by remember { mutableStateOf(ConvertCategory.LENGTH.units[3]) }
    var toUnit by remember { mutableStateOf(ConvertCategory.LENGTH.units[2]) }
    var input by remember { mutableStateOf("1") }

    LaunchedEffect(Unit) {
        val (catName, fromSym, toSym) = preferences.currentConverterUnits()
        val stored = ConvertCategory.entries.firstOrNull { it.name == catName } ?: return@LaunchedEffect
        category = stored
        fromUnit = stored.units.firstOrNull { it.symbol == fromSym } ?: stored.units.first()
        toUnit = stored.units.firstOrNull { it.symbol == toSym } ?: stored.units.last()
    }

    fun persist() {
        scope.launch {
            preferences.setConverterUnits(category.name, fromUnit.symbol, toUnit.symbol)
        }
    }

    val result = input.toDoubleOrNull()?.let {
        UnitConverter.convert(category, fromUnit.symbol, toUnit.symbol, it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Converter") },
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
            CategoryDropdown(
                selected = category,
                onSelect = {
                    category = it
                    fromUnit = it.units.first()
                    toUnit = it.units.last()
                    persist()
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UnitDropdown(
                    label = "From",
                    units = category.units,
                    selected = fromUnit,
                    onSelect = {
                        fromUnit = it
                        persist()
                    },
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    val tmp = fromUnit
                    fromUnit = toUnit
                    toUnit = tmp
                    persist()
                }) {
                    Icon(Icons.Filled.SwapVert, contentDescription = "Swap units")
                }
                UnitDropdown(
                    label = "To",
                    units = category.units,
                    selected = toUnit,
                    onSelect = {
                        toUnit = it
                        persist()
                    },
                    modifier = Modifier.weight(1f),
                )
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = result?.let { formatNumber(it) } ?: "—",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "${fromUnit.symbol} → ${toUnit.symbol}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private fun formatNumber(value: Double): String {
    val formatted = String.format("%.6f", value)
    return formatted.trimEnd('0').trimEnd('.').ifEmpty { "0" }
}

@Composable
private fun CategoryDropdown(
    selected: ConvertCategory,
    onSelect: (ConvertCategory) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Category: ${selected.title}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ConvertCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.title) },
                    onClick = {
                        expanded = false
                        onSelect(category)
                    },
                )
            }
        }
    }
}

@Composable
private fun UnitDropdown(
    label: String,
    units: List<ConvertUnit>,
    selected: ConvertUnit,
    onSelect: (ConvertUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("${selected.symbol}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text("${unit.name} (${unit.symbol})") },
                    onClick = {
                        expanded = false
                        onSelect(unit)
                    },
                )
            }
        }
    }
}
