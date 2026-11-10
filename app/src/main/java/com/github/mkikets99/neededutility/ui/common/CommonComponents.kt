package com.github.mkikets99.neededutility.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/**
 * Shared building blocks for tool screens.
 * Prefer these over private copies in new tool screens.
 */

/**
 * Standard tool top bar: title + back button + optional actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = actions,
    )
}

/**
 * Standard numeric input field (single line).
 */
@Composable
fun NumberField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
    )
}

/**
 * Shared label/value row used by calculator-style tools.
 * Prefer these over private copies in new tool screens.
 */
@Composable
fun ResultRow(
    label: String,
    value: Double?,
    suffix: String = "",
    pattern: String = "%.2f",
    modifier: Modifier = Modifier,
) {
    ResultRowText(
        label = label,
        value = value?.let { String.format(pattern, it) + suffix } ?: "—",
        modifier = modifier,
    )
}

/**
 * Label/value row with a pre-formatted value string.
 */
@Composable
fun ResultRowText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
