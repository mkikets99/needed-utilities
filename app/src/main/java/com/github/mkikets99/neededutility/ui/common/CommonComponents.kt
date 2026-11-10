package com.github.mkikets99.neededutility.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
