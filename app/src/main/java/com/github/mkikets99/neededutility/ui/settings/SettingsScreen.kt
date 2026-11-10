package com.github.mkikets99.neededutility.ui.settings

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mkikets99.neededutility.data.HomeLayoutMode
import com.github.mkikets99.neededutility.data.SettingsRepository
import com.github.mkikets99.neededutility.data.ThemeMode
import com.github.mkikets99.neededutility.monetization.BillingManager
import com.github.mkikets99.neededutility.monetization.PaywallDialog
import com.github.mkikets99.neededutility.monetization.ProState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    repository: SettingsRepository,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings by repository.settings.collectAsStateWithLifecycle(
        initialValue = com.github.mkikets99.neededutility.data.ThemeSettings()
    )
    val isPro by ProState.isPro.collectAsStateWithLifecycle()
    var showPaywall by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Settings", onBack = onBack)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text("Pro", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isPro) "Pro is active — thank you!"
                        else "Remove ads & unlock unlimited items",
                    )
                    Text(
                        text = "One-time purchase, no subscription",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (!isPro) {
                    OutlinedButton(onClick = { showPaywall = true }) {
                        Text("Get Pro")
                    }
                }
            }
            TextButton(onClick = {
                BillingManager.restorePurchases(context)
            }) {
                Text("Restore purchases")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Appearance", style = MaterialTheme.typography.titleMedium)
            ThemeMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = settings.mode == mode,
                            onClick = { scope.launch { repository.setThemeMode(mode) } },
                            role = Role.RadioButton,
                        )
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = settings.mode == mode,
                        onClick = null,
                    )
                    Text(
                        text = when (mode) {
                            ThemeMode.SYSTEM -> "Follow system"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                        },
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Home layout", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                HomeLayoutMode.entries.forEach { layout ->
                    FilterChip(
                        selected = settings.homeLayout == layout,
                        onClick = { scope.launch { repository.setHomeLayout(layout) } },
                        label = {
                            Text(if (layout == HomeLayoutMode.CARDS) "Cards" else "List")
                        },
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dynamic color")
                    Text(
                        text = "Material You colors from your wallpaper (Android 12+)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = settings.dynamicColor,
                    onCheckedChange = { enabled ->
                        scope.launch { repository.setDynamicColor(enabled) }
                    },
                )
            }
        }
    }

    if (showPaywall) {
        PaywallDialog(onDismiss = { showPaywall = false })
    }
}
