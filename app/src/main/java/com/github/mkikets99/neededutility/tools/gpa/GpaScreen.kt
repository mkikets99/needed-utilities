package com.github.mkikets99.neededutility.tools.gpa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box

private data class Course(val id: Long, val grade: String, val credits: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaScreen(onBack: () -> Unit) {
    var courses by remember { mutableStateOf(listOf<Course>()) }
    var showDialog by remember { mutableStateOf(false) }

    val gpa = GpaCalculator.calculate(courses.map { it.grade to it.credits })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPA Calculator") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add course")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("GPA (US 4.0 scale)", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = gpa?.let { String.format("%.2f", it) } ?: "—",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            if (courses.isEmpty()) {
                Text(
                    text = "No courses yet. Tap + to add one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                ) {
                    items(courses, key = { it.id }) { course ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${course.grade} — ${course.credits} credits",
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                IconButton(onClick = {
                                    courses = courses.filterNot { it.id == course.id }
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Remove course")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddCourseDialog(
            onSave = { grade, credits ->
                courses = courses + Course(
                    id = System.currentTimeMillis(),
                    grade = grade,
                    credits = credits,
                )
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
private fun AddCourseDialog(
    onSave: (String, Double) -> Unit,
    onDismiss: () -> Unit,
) {
    var grade by remember { mutableStateOf("A") }
    var creditsInput by remember { mutableStateOf("3") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add course") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Grade: $grade (${GpaCalculator.GRADE_POINTS[grade]})")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        GpaCalculator.GRADE_POINTS.keys.forEach { entry ->
                            DropdownMenuItem(
                                text = { Text("$entry (${GpaCalculator.GRADE_POINTS[entry]})") },
                                onClick = {
                                    grade = entry
                                    expanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = creditsInput,
                    onValueChange = { creditsInput = it },
                    label = { Text("Credits") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val credits = creditsInput.toDoubleOrNull()
                    if (credits != null && credits > 0) onSave(grade, credits)
                },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
