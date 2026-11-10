package com.github.mkikets99.neededutility.tools.teams

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSplitterScreen(onBack: () -> Unit) {
    var namesInput by remember { mutableStateOf("") }
    var teamCount by remember { mutableIntStateOf(2) }
    var teams by remember { mutableStateOf(listOf<List<String>>()) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Team Splitter", onBack = onBack)
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
            OutlinedTextField(
                value = namesInput,
                onValueChange = { namesInput = it },
                label = { Text("Names (one per line)") },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Teams: $teamCount", style = MaterialTheme.typography.titleSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (teamCount > 1) teamCount-- }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Fewer teams")
                    }
                    Text("$teamCount", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { if (teamCount < 20) teamCount++ }) {
                        Icon(Icons.Filled.Add, contentDescription = "More teams")
                    }
                }
            }

            Button(
                onClick = {
                    val result = TeamSplitter.split(namesInput.lines(), teamCount)
                    if (result == null) {
                        error = "You need at least $teamCount names."
                        teams = emptyList()
                    } else {
                        error = null
                        teams = result
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Split into teams")
            }

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            teams.forEachIndexed { index, team ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Team ${index + 1}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = team.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }
    }
}
