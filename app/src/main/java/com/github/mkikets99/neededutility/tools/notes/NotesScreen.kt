package com.github.mkikets99.neededutility.tools.notes

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mkikets99.neededutility.monetization.Limits
import com.github.mkikets99.neededutility.monetization.PaywallDialog
import com.github.mkikets99.neededutility.monetization.ProState
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { NotesRepository(context.applicationContext) }
    val scope = rememberCoroutineScope()
    val notes by repository.notes.collectAsStateWithLifecycle(initialValue = emptyList())

    var editing by remember { mutableStateOf<Note?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<String?>(null) }
    var showPaywall by remember { mutableStateOf(false) }

    val isPro by ProState.isPro.collectAsStateWithLifecycle()

    val allTags = notes.map { it.tag.trim() }.filter { it.isNotBlank() }.distinct().sorted()

    val visibleNotes = notes.filter { note ->
        val matchesQuery = query.isBlank() ||
            note.title.contains(query, ignoreCase = true) ||
            note.body.contains(query, ignoreCase = true) ||
            note.tag.contains(query, ignoreCase = true)
        val matchesTag = selectedTag == null || note.tag.trim().equals(selectedTag, ignoreCase = true)
        matchesQuery && matchesTag
    }

    Scaffold(
        topBar = {
            ToolTopBar(title = "Notes", onBack = onBack)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (Limits.canAdd(notes.size, isPro, Limits.MAX_NOTES)) {
                        editing = null
                        showDialog = true
                    } else {
                        showPaywall = true
                    }
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add note")
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search notes") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (allTags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    allTags.take(8).forEach { tag ->
                        FilterChip(
                            selected = selectedTag == tag,
                            onClick = {
                                selectedTag = if (selectedTag == tag) null else tag
                            },
                            label = { Text("#$tag") },
                        )
                    }
                }
            }
            if (visibleNotes.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = if (notes.isEmpty()) {
                            "No notes yet.\nTap + to add one."
                        } else {
                            "No notes match \"$query\"."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(visibleNotes, key = { it.id }) { note ->
                    Card(
                        onClick = {
                            editing = note
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = note.title.ifBlank { "Untitled" },
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (note.tag.isNotBlank()) {
                                Text(
                                    text = "#${note.tag}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                            if (note.body.isNotBlank()) {
                                Text(
                                    text = note.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                            Text(
                                text = DateFormat.getDateTimeInstance().format(Date(note.updatedAt)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(top = 8.dp),
                            )
                        }
                    }
                }
            }
        }
        }
    }

    if (showDialog) {
        NoteDialog(
            note = editing,
            onSave = { title, body, tag ->
                val existing = editing
                scope.launch {
                    repository.upsert(
                        Note(
                            id = existing?.id ?: System.currentTimeMillis(),
                            title = title,
                            body = body,
                            updatedAt = System.currentTimeMillis(),
                            tag = tag,
                        )
                    )
                }
                showDialog = false
            },
            onDelete = { note ->
                scope.launch { repository.delete(note.id) }
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }

    if (showPaywall) {
        PaywallDialog(onDismiss = { showPaywall = false })
    }
}

@Composable
private fun NoteDialog(
    note: Note?,
    onSave: (title: String, body: String, tag: String) -> Unit,
    onDelete: (Note) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var body by remember { mutableStateOf(note?.body ?: "") }
    var tag by remember { mutableStateOf(note?.tag ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (note == null) "New note" else "Edit note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Note") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text("Tag (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, body, tag) },
                enabled = title.isNotBlank() || body.isNotBlank(),
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            if (note != null) {
                TextButton(onClick = { onDelete(note) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
