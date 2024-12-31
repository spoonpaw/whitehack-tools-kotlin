package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.components.BasicInfoDetailCard
import com.example.whitehacktools.ui.components.TopBarAction
import com.example.whitehacktools.ui.components.WhitehackTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: PlayerCharacter,
    onNavigateBack: () -> Unit,
    onEditCharacter: (PlayerCharacter) -> Unit
) {
    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = "Character Detail",
                onNavigateBack = onNavigateBack,
                actions = listOf(
                    TopBarAction.IconAction(
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit Character",
                        onClick = { onEditCharacter(character) }
                    )
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicInfoDetailCard(
                character = character,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Add more sections here as needed
        }
    }
}
