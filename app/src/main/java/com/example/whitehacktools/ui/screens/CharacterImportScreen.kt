package com.example.whitehacktools.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterImportScreen(
    onCharacterImported: (PlayerCharacter) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var importError by remember { mutableStateOf<String?>(null) }

    val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    val character = json.decodeFromString<PlayerCharacter>(jsonString)
                    onCharacterImported(character)
                }
            } catch (e: Exception) {
                importError = "Failed to import character: ${e.message}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Import Character",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = { importLauncher.launch("application/json") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select JSON File")
        }

        importError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
