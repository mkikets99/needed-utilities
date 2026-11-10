package com.github.mkikets99.neededutility.tools.worldclock

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private data class ClockZone(val city: String, val zoneId: String)

private val clockZones = listOf(
    ClockZone("Los Angeles", "America/Los_Angeles"),
    ClockZone("New York", "America/New_York"),
    ClockZone("London", "Europe/London"),
    ClockZone("Kyiv", "Europe/Kyiv"),
    ClockZone("Berlin", "Europe/Berlin"),
    ClockZone("Dubai", "Asia/Dubai"),
    ClockZone("Tokyo", "Asia/Tokyo"),
    ClockZone("Sydney", "Australia/Sydney"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldClockScreen(onBack: () -> Unit) {
    var now by remember { mutableStateOf(Instant.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = Instant.now()
            delay(1000)
        }
    }

    Scaffold(
        topBar = {
            ToolTopBar(title = "World Clock", onBack = onBack)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(clockZones) { clockZone ->
                val zone = ZoneId.of(clockZone.zoneId)
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(zone)
                val dateFormatter = DateTimeFormatter.ofPattern("EEE, d MMM").withZone(zone)
                val offset = zone.rules.getOffset(now).totalSeconds / 3600.0

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(clockZone.city, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = dateFormatter.format(now) +
                                    "  (UTC" + (if (offset >= 0) "+" else "") +
                                    String.format("%.0f", offset) + ")",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            text = timeFormatter.format(now),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}
