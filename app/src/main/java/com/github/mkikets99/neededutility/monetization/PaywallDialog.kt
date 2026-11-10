package com.github.mkikets99.neededutility.monetization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.mkikets99.neededutility.monetization.findActivity as findHostActivity

/**
 * One-time Pro purchase dialog: removes ads + lifts free-tier caps.
 * Shown from Settings and whenever a free-tier cap is hit.
 */
@Composable
fun PaywallDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var price by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        BillingManager.queryPrice { price = it }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Get Pro") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "One-time purchase — no subscription. Unlocks:",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text("• No ads anywhere", style = MaterialTheme.typography.bodyMedium)
                Text("• Unlimited notes & countdowns", style = MaterialTheme.typography.bodyMedium)
                Text("• Unlimited fuel & trip entries", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = price?.let { "Price: $it (one-time)" }
                        ?: "One-time purchase via Google Play",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                context.findHostActivity()?.let { BillingManager.launchPurchase(it) }
                onDismiss()
            }) {
                Text("Buy Pro")
            }
        },
        dismissButton = {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TextButton(onClick = {
                    BillingManager.restorePurchases(context)
                    onDismiss()
                }) {
                    Text("Restore")
                }
                TextButton(onClick = onDismiss) {
                    Text("Not now")
                }
            }
        },
    )
}
