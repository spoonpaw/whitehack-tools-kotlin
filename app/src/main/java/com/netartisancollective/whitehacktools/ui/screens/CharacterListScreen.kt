package com.netartisancollective.whitehacktools.ui.screens

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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.PlayerCharacter
import com.netartisancollective.whitehacktools.ui.components.TopBarAction
import com.netartisancollective.whitehacktools.ui.components.WhitehackTopAppBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.netartisancollective.whitehacktools.data.CharacterStore
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import com.netartisancollective.whitehacktools.util.CrossPlatformConverter
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    characters: List<PlayerCharacter> = emptyList(),
    onAddCharacter: () -> Unit = {},
    onSelectCharacter: (PlayerCharacter) -> Unit = {},
    onDeleteCharacter: (PlayerCharacter) -> Unit = {},
    characterStore: CharacterStore
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var characterToDelete by remember { mutableStateOf<PlayerCharacter?>(null) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importError by remember { mutableStateOf<String?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    var selectedCharacters by remember { mutableStateOf(setOf<PlayerCharacter>()) }
    var importedCharacters by remember { mutableStateOf<List<PlayerCharacter>>(emptyList()) }
    var showImportSelectionDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    fun parseCharactersFromJson(jsonString: String): List<PlayerCharacter> {
        return try {
            Log.d("Import", "Parsing JSON data...")
            
            // First, parse as raw JSON to detect format
            val rawJson = Json.parseToJsonElement(jsonString)
            
            // Check if this is Swift format and convert if needed
            val finalJsonString = if (CrossPlatformConverter.isSwiftFormat(rawJson)) {
                Log.d("Import", "🔄 Detected Swift format - converting to Kotlin format...")
                val converted = CrossPlatformConverter.convertFromSwift(rawJson)
                when (converted) {
                    is JsonArray -> Json.encodeToString(JsonArray.serializer(), converted)
                    is JsonObject -> Json.encodeToString(JsonObject.serializer(), converted)
                    else -> jsonString
                }
            } else {
                Log.d("Import", "✅ Detected native Kotlin format - no conversion needed")
                jsonString
            }
            
            Log.d("Import", "Attempting to parse as list...")
            try {
                // Parse and generate new IDs for each character
                json.decodeFromString<List<PlayerCharacter>>(finalJsonString).map { it.copyWithNewId() }
            } catch (e: Exception) {
                Log.e("Import", "Failed to parse as list: ${e.message}", e)
                Log.d("Import", "Attempting to parse as single character...")
                try {
                    // Try parsing as single character and generate new ID
                    listOf(json.decodeFromString<PlayerCharacter>(finalJsonString).copyWithNewId())
                } catch (e: Exception) {
                    Log.e("Import", "Failed to parse as single character: ${e.message}", e)
                    throw Exception("Invalid character data format: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("Import", "Failed to parse characters: ${e.message}", e)
            throw e
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { 
            try {
                val jsonString = json.encodeToString(selectedCharacters.toList())
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to export: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                Log.d("Import", "URI selected: $uri")
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    Log.d("Import", "JSON read: $jsonString")
                    try {
                        importedCharacters = parseCharactersFromJson(jsonString)
                        Log.d("Import", "Characters parsed: ${importedCharacters.size}")
                        showImportDialog = false
                        showImportSelectionDialog = true
                        selectedCharacters = emptySet()
                        importError = null
                    } catch (e: Exception) {
                        Log.e("Import", "Failed to parse characters", e)
                        importError = "Failed to parse characters: ${e.message}"
                        Toast.makeText(context, "Failed to parse characters: ${e.message}", Toast.LENGTH_LONG).show()
                        showImportDialog = true
                    }
                } ?: run {
                    Log.e("Import", "Failed to open input stream")
                    importError = "Failed to read file"
                    Toast.makeText(context, "Failed to read file", Toast.LENGTH_LONG).show()
                    showImportDialog = true
                }
            } catch (e: Exception) {
                Log.e("Import", "Failed to import", e)
                importError = "Failed to import: ${e.message}"
                Toast.makeText(context, "Failed to import: ${e.message}", Toast.LENGTH_LONG).show()
                showImportDialog = true
            }
        } ?: run {
            Log.e("Import", "No URI selected")
            importError = "No file selected"
            Toast.makeText(context, "No file selected", Toast.LENGTH_LONG).show()
            showImportDialog = true
        }
    }

    if (showImportSelectionDialog) {
        AlertDialog(
            onDismissRequest = { 
                showImportSelectionDialog = false
                selectedCharacters = emptySet()
                importedCharacters = emptyList()
            },
            title = { 
                Text(
                    text = "Select Characters to Import",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { selectedCharacters = importedCharacters.toSet() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Select All")
                        }
                        TextButton(
                            onClick = { selectedCharacters = emptySet() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Deselect All")
                        }
                    }
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(importedCharacters) { character ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = character in selectedCharacters,
                                    onCheckedChange = { checked ->
                                        selectedCharacters = if (checked) {
                                            selectedCharacters + character
                                        } else {
                                            selectedCharacters - character
                                        }
                                    }
                                )
                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (selectedCharacters.isNotEmpty()) {
                            scope.launch {
                                // Merge selected characters with existing ones
                                val updatedCharacters = characters + selectedCharacters.toList()
                                characterStore.saveCharacters(updatedCharacters)
                                showImportSelectionDialog = false
                                selectedCharacters = emptySet()
                                importedCharacters = emptyList()
                            }
                        }
                    },
                    enabled = selectedCharacters.isNotEmpty()
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showImportSelectionDialog = false
                        selectedCharacters = emptySet()
                        importedCharacters = emptyList()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { 
                showImportDialog = false
                importError = null 
            },
            title = { 
                Text(
                    text = "Import Character",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Choose how to import your character:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    importError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { 
                                importLauncher.launch(arrayOf("application/json"))
                                Log.d("Import", "Launching file picker")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("From File")
                        }
                        FilledTonalButton(
                            onClick = {
                                clipboardManager.getText()?.text?.let { clipboardText ->
                                    try {
                                        importedCharacters = parseCharactersFromJson(clipboardText)
                                        showImportDialog = false
                                        showImportSelectionDialog = true
                                        selectedCharacters = emptySet()
                                        importError = null
                                    } catch (e: Exception) {
                                        importError = e.message ?: "Failed to import character"
                                    }
                                } ?: run {
                                    importError = "No text found in clipboard"
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("From Clipboard")
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showImportDialog = false
                        importError = null 
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { 
                showExportDialog = false
                selectedCharacters = emptySet()
            },
            title = { 
                Text(
                    text = "Export Characters",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { selectedCharacters = characters.toSet() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Select All")
                        }
                        TextButton(
                            onClick = { selectedCharacters = emptySet() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Deselect All")
                        }
                    }
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(characters) { character ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = character in selectedCharacters,
                                    onCheckedChange = { checked ->
                                        selectedCharacters = if (checked) {
                                            selectedCharacters + character
                                        } else {
                                            selectedCharacters - character
                                        }
                                    }
                                )
                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (selectedCharacters.isNotEmpty()) {
                            exportLauncher.launch("characters.json")
                        }
                        showExportDialog = false
                    },
                    enabled = selectedCharacters.isNotEmpty()
                ) {
                    Text("Export")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showExportDialog = false
                        selectedCharacters = emptySet()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

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
                        onClick = { showImportDialog = true }
                    ),
                    TopBarAction.TonalButtonAction(
                        text = "Export",
                        onClick = { 
                            if (characters.isNotEmpty()) {
                                showExportDialog = true
                            }
                        }
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
        onClick = onSelect,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${character.characterClass} Level ${character.level}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (character.species.isNotBlank() || character.vocation.isNotBlank()) {
                    Text(
                        text = listOfNotNull(
                            character.species.takeIf { it.isNotBlank() },
                            character.vocation.takeIf { it.isNotBlank() }
                        ).joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Character",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
