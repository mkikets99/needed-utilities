package com.github.mkikets99.neededutility.tools.qr

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

private const val QR_SIZE = 512

private enum class QrMode(val label: String) {
    TEXT("Text / URL"),
    WIFI("WiFi network"),
    CONTACT("Contact"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGeneratorScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf(QrMode.TEXT) }
    var text by remember { mutableStateOf("") }
    var ssid by remember { mutableStateOf("") }
    var wifiPassword by remember { mutableStateOf("") }
    var hidden by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var contactOrg by remember { mutableStateOf("") }

    val payload = when (mode) {
        QrMode.TEXT -> text
        QrMode.WIFI -> WifiQr.build(ssid, wifiPassword, hidden)
        QrMode.CONTACT -> ContactQr.build(
            name = contactName,
            phone = contactPhone,
            email = contactEmail,
            organization = contactOrg,
        ) ?: ""
    }
    val bitmap = remember(payload) {
        if (payload.isBlank()) null
        else runCatching { encodeQr(payload, QR_SIZE) }.getOrNull()
    }

    Scaffold(
        topBar = {
            ToolTopBar(title = "QR Generator", onBack = onBack)
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
                QrMode.entries.forEach { entry ->
                    FilterChip(
                        selected = mode == entry,
                        onClick = { mode = entry },
                        label = { Text(entry.label) },
                    )
                }
            }

            when (mode) {
                QrMode.TEXT -> {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Text or URL") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                QrMode.WIFI -> {
                    OutlinedTextField(
                        value = ssid,
                        onValueChange = { ssid = it },
                        label = { Text("Network name (SSID)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = wifiPassword,
                        onValueChange = { wifiPassword = it },
                        label = { Text("Password (empty = open network)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Hidden network")
                        Switch(checked = hidden, onCheckedChange = { hidden = it })
                    }
                }

                QrMode.CONTACT -> {
                    OutlinedTextField(
                        value = contactName,
                        onValueChange = { contactName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = contactEmail,
                        onValueChange = { contactEmail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = contactOrg,
                        onValueChange = { contactOrg = it },
                        label = { Text("Organization (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            if (bitmap != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Generated QR code",
                        modifier = Modifier.size(256.dp),
                    )
                    Text(
                        text = when (mode) {
                            QrMode.TEXT -> "QR code for ${text.length} character(s)"
                            QrMode.WIFI -> "WiFi QR for \"${ssid.ifBlank { "network" }}\" — scan to join"
                            QrMode.CONTACT -> "Contact card for \"${contactName.ifBlank { "someone" }}\""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Text(
                    text = when (mode) {
                        QrMode.TEXT -> "Type something to generate a QR code."
                        QrMode.WIFI -> "Enter an SSID to generate a WiFi QR code."
                        QrMode.CONTACT -> "Enter a name to generate a contact QR code."
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

internal fun encodeQr(content: String, size: Int): Bitmap {
    val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}
