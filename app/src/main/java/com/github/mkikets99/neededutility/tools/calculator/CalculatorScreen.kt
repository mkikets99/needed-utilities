package com.github.mkikets99.neededutility.tools.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    val engine = remember { CalculatorEngine() }
    var display by remember { mutableStateOf(engine.display) }

    fun refresh() {
        display = engine.display
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculator") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = display,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )

            val rows = listOf(
                listOf("AC", "±", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "−"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "="),
            )

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    row.forEach { key ->
                        CalcButton(
                            key = key,
                            onClick = {
                                when (key) {
                                    "AC" -> engine.clear()
                                    "±" -> engine.toggleSign()
                                    "%" -> engine.percent()
                                    "=" -> engine.equals()
                                    "+" -> engine.inputOp('+')
                                    "−" -> engine.inputOp('-')
                                    "×" -> engine.inputOp('*')
                                    "÷" -> engine.inputOp('/')
                                    "." -> engine.inputDot()
                                    else -> engine.inputDigit(key.toInt())
                                }
                                refresh()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                        )
                    }
                    if (row.size == 3) {
                        // keep the layout aligned; "0" row has three wide keys
                    }
                }
            }
        }
    }
}

@Composable
private fun CalcButton(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOperator = key in listOf("÷", "×", "−", "+", "=")
    val isFunction = key in listOf("AC", "±", "%")

    when {
        isOperator -> Button(onClick = onClick, modifier = modifier) {
            Text(key, style = MaterialTheme.typography.titleLarge)
        }

        isFunction -> FilledTonalButton(onClick = onClick, modifier = modifier) {
            Text(key, style = MaterialTheme.typography.titleLarge)
        }

        else -> OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(key, style = MaterialTheme.typography.titleLarge)
        }
    }
}
