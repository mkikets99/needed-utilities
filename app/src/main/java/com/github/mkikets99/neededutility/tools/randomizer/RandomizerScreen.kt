package com.github.mkikets99.neededutility.tools.randomizer

import com.github.mkikets99.neededutility.ui.common.ToolTopBar
import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

private enum class RandomizerMode(val label: String) {
    DICE("Dice"),
    COIN("Coin"),
    NUMBER("Number"),
    PASSWORD("Password"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomizerScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf(RandomizerMode.DICE) }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Randomizer", onBack = onBack)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PrimaryTabRow(selectedTabIndex = mode.ordinal) {
                RandomizerMode.entries.forEach { entry ->
                    Tab(
                        selected = mode == entry,
                        onClick = { mode = entry },
                        text = { Text(entry.label) },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when (mode) {
                    RandomizerMode.DICE -> DiceTab()
                    RandomizerMode.COIN -> CoinTab()
                    RandomizerMode.NUMBER -> NumberTab()
                    RandomizerMode.PASSWORD -> PasswordTab()
                }
            }
        }
    }
}

@Composable
private fun DiceTab() {
    var diceCount by remember { mutableIntStateOf(2) }
    var dieSides by remember { mutableIntStateOf(6) }
    var rolls by remember { mutableStateOf(listOf<Int>()) }

    Text("Die type: d$dieSides", style = MaterialTheme.typography.titleSmall)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(4, 6, 8, 10, 12, 20).forEach { sides ->
            FilterChip(
                selected = dieSides == sides,
                onClick = {
                    dieSides = sides
                    rolls = emptyList()
                },
                label = { Text("d$sides") },
            )
        }
    }

    Text("Dice: $diceCount", style = MaterialTheme.typography.titleSmall)
    Slider(
        value = diceCount.toFloat(),
        onValueChange = { diceCount = it.toInt() },
        valueRange = 1f..6f,
        steps = 4,
    )
    Button(
        onClick = { rolls = List(diceCount) { Random.nextInt(1, dieSides + 1) } },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Roll")
    }
    if (rolls.isNotEmpty()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = rolls.joinToString("  •  "),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Total: ${rolls.sum()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun CoinTab() {
    var result by remember { mutableStateOf<String?>(null) }

    Button(
        onClick = { result = if (Random.nextBoolean()) "Heads" else "Tails" },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Flip coin")
    }
    result?.let {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = it,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            )
        }
    }
}

@Composable
private fun NumberTab() {
    var minInput by remember { mutableStateOf("1") }
    var maxInput by remember { mutableStateOf("100") }
    var result by remember { mutableStateOf<Int?>(null) }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = minInput,
            onValueChange = { minInput = it.filter(Char::isDigit).take(9) },
            label = { Text("Min") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = maxInput,
            onValueChange = { maxInput = it.filter(Char::isDigit).take(9) },
            label = { Text("Max") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Button(
        onClick = {
            val min = minInput.toIntOrNull() ?: 0
            val max = maxInput.toIntOrNull() ?: 0
            result = if (max >= min) Random.nextInt(min, max + 1) else min
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Generate")
    }
    result?.let {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = it.toString(),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            )
        }
    }
}

@Composable
private fun PasswordTab() {
    var length by remember { mutableFloatStateOf(16f) }
    var useUppercase by remember { mutableStateOf(true) }
    var useDigits by remember { mutableStateOf(true) }
    var useSymbols by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Text("Length: ${length.toInt()}", style = MaterialTheme.typography.titleSmall)
    Slider(
        value = length,
        onValueChange = { length = it },
        valueRange = 8f..64f,
        steps = 55,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Uppercase (A–Z)")
        Switch(checked = useUppercase, onCheckedChange = { useUppercase = it })
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Digits (0–9)")
        Switch(checked = useDigits, onCheckedChange = { useDigits = it })
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Symbols (!@#…)")
        Switch(checked = useSymbols, onCheckedChange = { useSymbols = it })
    }

    Button(
        onClick = {
            password = generatePassword(
                length = length.toInt(),
                uppercase = useUppercase,
                digits = useDigits,
                symbols = useSymbols,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Generate password")
    }

    if (password.isNotEmpty()) {
        val strength = PasswordStrengthEvaluator.evaluate(password)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Strength", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = strength.label,
                style = MaterialTheme.typography.titleMedium,
                color = when (strength) {
                    PasswordStrength.WEAK -> MaterialTheme.colorScheme.error
                    PasswordStrength.FAIR -> MaterialTheme.colorScheme.tertiary
                    PasswordStrength.GOOD -> MaterialTheme.colorScheme.secondary
                    PasswordStrength.STRONG -> MaterialTheme.colorScheme.primary
                },
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = password,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    scope.launch {
                        clipboard.setClipEntry(
                            ClipEntry(ClipData.newPlainText("password", password))
                        )
                    }
                }) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy password")
                }
            }
        }
    }
}

internal fun generatePassword(
    length: Int,
    uppercase: Boolean,
    digits: Boolean,
    symbols: Boolean,
): String {
    val lowers = "abcdefghijklmnopqrstuvwxyz"
    val uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val digitChars = "0123456789"
    val symbolChars = "!@#\$%^&*()-_=+[]{};:,.?"
    val pool = lowers +
        (if (uppercase) uppers else "") +
        (if (digits) digitChars else "") +
        (if (symbols) symbolChars else "")
    return buildString {
        repeat(length) { append(pool[Random.nextInt(pool.length)]) }
    }
}
