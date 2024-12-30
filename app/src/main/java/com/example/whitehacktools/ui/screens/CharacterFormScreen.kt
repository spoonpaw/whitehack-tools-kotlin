package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    initialName: String = "",
    initialLevel: Int = 1,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onSave: (name: String, level: Int) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var level by remember { mutableStateOf(initialLevel.toString()) }
    
    val isValid = name.isNotBlank() && level.toIntOrNull() in 1..10
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialName.isEmpty()) "New Character" else "Edit Character") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { 
                            onSave(name, level.toIntOrNull() ?: 1)
                        },
                        enabled = isValid
                    ) {
                        Text("Save")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Character Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = level,
                onValueChange = { input ->
                    if (input.isEmpty() || input.toIntOrNull() in 1..10) {
                        level = input
                    }
                },
                label = { Text("Level (1-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
