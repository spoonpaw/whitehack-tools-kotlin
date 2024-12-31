package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whitehacktools.ui.components.BasicInfoFormCard

private val characterClasses = listOf(
    "Deft",
    "Strong",
    "Wise",
    "Brave",
    "Clever",
    "Fortunate"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    initialName: String = "",
    initialPlayerName: String = "",
    initialLevel: Int = 1,
    initialCharacterClass: String = "Deft",
    onNavigateBack: () -> Unit = {},
    onSave: (name: String, level: Int, characterClass: String) -> Unit = { _, _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var playerName by remember { mutableStateOf(initialPlayerName) }
    var level by remember { mutableStateOf(initialLevel.toString()) }
    var characterClass by remember { mutableStateOf(initialCharacterClass) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialName.isEmpty()) "New Character" else "Edit Character") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val levelInt = level.toIntOrNull() ?: 1
                            onSave(name, levelInt, characterClass)
                        },
                        enabled = name.isNotBlank() && level.toIntOrNull() in 1..10
                    ) {
                        Text(
                            "Save",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicInfoFormCard(
                name = name,
                onNameChange = { name = it },
                playerName = playerName,
                onPlayerNameChange = { playerName = it },
                level = level,
                onLevelChange = { level = it },
                characterClass = characterClass,
                onCharacterClassChange = { characterClass = it },
                characterClasses = characterClasses,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
