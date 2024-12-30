package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    initialName: String = "",
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    var name by remember { mutableStateOf(initialName) }
    
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
                            onSave(name)
                        },
                        enabled = name.isNotBlank()
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Character Name") },
                modifier = Modifier.fillMaxWidth()
            )
            // TODO: Add more form fields (class, level, etc.)
        }
    }
}
