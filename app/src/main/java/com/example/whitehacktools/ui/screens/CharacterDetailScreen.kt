package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: PlayerCharacter,
    onNavigateBack: () -> Unit,
    onEditCharacter: (PlayerCharacter, com.example.whitehacktools.ui.components.CharacterTab) -> Unit,
    initialTab: com.example.whitehacktools.ui.components.CharacterTab = com.example.whitehacktools.ui.components.CharacterTab.Info
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = "Character Detail",
                onNavigateBack = onNavigateBack,
                actions = listOf(
                    TopBarAction.IconAction(
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit Character",
                        onClick = { onEditCharacter(character, selectedTab) }
                    )
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CharacterTabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            
            when (selectedTab) {
                com.example.whitehacktools.ui.components.CharacterTab.Info -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BasicInfoDetailCard(
                            character = character,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                com.example.whitehacktools.ui.components.CharacterTab.Combat -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionCard(
                            title = "Combat Stats",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Combat stats coming soon...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                com.example.whitehacktools.ui.components.CharacterTab.Equipment -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionCard(
                            title = "Equipment",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Equipment details coming soon...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
