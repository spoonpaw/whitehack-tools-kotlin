package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.components.*
import com.example.whitehacktools.ui.models.CharacterTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: PlayerCharacter,
    initialTab: CharacterTab = CharacterTab.Info,
    onNavigateBack: () -> Unit = {},
    onEdit: (CharacterTab) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = "Character Detail",
                onNavigateBack = onNavigateBack,
                actions = listOf(
                    TopBarAction.IconAction(
                        icon = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        onClick = { onEdit(selectedTab) }
                    )
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                tabs = {
                    CharacterTab.values().forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(text = tab.title) }
                        )
                    }
                }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    CharacterTab.Info -> {
                        BasicInfoDetailCard(
                            character = character,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        AttributesDetailCard(
                            strength = character.strength,
                            agility = character.agility,
                            toughness = character.toughness,
                            intelligence = character.intelligence,
                            willpower = character.willpower,
                            charisma = character.charisma,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CharacterTab.Combat -> {
                        CombatDetailCard(
                            character = character,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CharacterTab.Equipment -> {
                        GoldDetailCard(
                            goldOnHand = character.goldOnHand,
                            stashedGold = character.stashedGold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
