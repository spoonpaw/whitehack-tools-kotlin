package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    characters: List<PlayerCharacter> = emptyList(),
    onAddCharacter: () -> Unit = {},
    onImportCharacter: () -> Unit = {},
    onExportCharacter: () -> Unit = {}
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text("Characters") },
                actions = {
                    // Import button
                    androidx.compose.material3.TextButton(onClick = onImportCharacter) {
                        androidx.compose.material3.Text("Import")
                    }
                    // Export button
                    androidx.compose.material3.TextButton(onClick = onExportCharacter) {
                        androidx.compose.material3.Text("Export")
                    }
                    // Add button
                    androidx.compose.material3.IconButton(onClick = onAddCharacter) {
                        androidx.compose.material3.Text("+", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (characters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "No characters yet",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(characters) { character ->
                    CharacterListItem(character = character)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListItem(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { /* TODO: Navigate to character detail */ }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            androidx.compose.material3.Text(
                text = character.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = "Level ${character.level} ${character.characterClass}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
