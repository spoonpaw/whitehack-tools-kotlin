package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.model.GroupType
import com.example.whitehacktools.ui.components.TopBarAction
import com.example.whitehacktools.ui.components.WhitehackTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    characters: List<PlayerCharacter> = emptyList(),
    onAddCharacter: () -> Unit = {},
    onSelectCharacter: (PlayerCharacter) -> Unit = {},
    onImportCharacter: () -> Unit = {},
    onExportCharacter: () -> Unit = {},
    onDeleteCharacter: (PlayerCharacter) -> Unit = {}
) {
    var characterToDelete by remember { mutableStateOf<PlayerCharacter?>(null) }

    characterToDelete?.let { character ->
        AlertDialog(
            onDismissRequest = { characterToDelete = null },
            title = { Text("Delete Character") },
            text = { Text("Are you sure you want to delete ${character.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCharacter(character)
                        characterToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { characterToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = "Characters",
                actions = listOf(
                    TopBarAction.TonalButtonAction(
                        text = "Import",
                        onClick = onImportCharacter
                    ),
                    TopBarAction.TonalButtonAction(
                        text = "Export",
                        onClick = onExportCharacter
                    ),
                    TopBarAction.IconAction(
                        icon = Icons.Default.Add,
                        contentDescription = "Add Character",
                        onClick = onAddCharacter
                    )
                )
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
                Text(
                    text = "No characters yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(characters) { character ->
                    CharacterListItem(
                        character = character,
                        onSelect = { onSelectCharacter(character) },
                        onDelete = { characterToDelete = character }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListItem(
    character: PlayerCharacter,
    onSelect: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Level ${character.level} ${character.characterClass}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${character.species.ifBlank { "No Species" }} • ${character.vocation.ifBlank { "No Vocation" }}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Character",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
