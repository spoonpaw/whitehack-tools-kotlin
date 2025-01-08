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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: PlayerCharacter,
    initialTab: CharacterTab = CharacterTab.Info,
    onNavigateBack: () -> Unit = {},
    onEdit: (CharacterTab) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    val scrollState = rememberScrollState()

    // Scroll to top when tab changes or when screen is shown with new character
    LaunchedEffect(selectedTab, character.id) {
        scrollState.scrollTo(0)
    }

    DisposableEffect(selectedTab) {
        onDispose { }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        CharacterTab.Info -> {
                            BasicInfoDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            AttributesDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            GroupsDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            LanguagesDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            when (character.characterClass) {
                                "Deft" -> DeftDetailCard(
                                    character = character,
                                    onCharacterChange = { /* no-op */ },  // This is a detail view, so no changes needed
                                    modifier = Modifier.fillMaxWidth()
                                )
                                "Strong" -> StrongDetailCard(character = character, modifier = Modifier.fillMaxWidth())
                                "Wise" -> WiseDetailCard(character = character, modifier = Modifier.fillMaxWidth())
                                "Brave" -> BraveDetailCard(character = character, modifier = Modifier.fillMaxWidth())
                                "Clever" -> CleverDetailCard(character = character, modifier = Modifier.fillMaxWidth())
                                "Fortunate" -> FortunateDetailCard(character = character, modifier = Modifier.fillMaxWidth())
                            }
                            
                            AdditionalInfoDetailCard(
                                experience = character.experience,
                                corruption = character.corruption,
                                notes = character.notes,
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
                            WeaponDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            ArmorDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            EquipmentDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                            GoldDetailCard(
                                goldOnHand = character.goldOnHand,
                                stashedGold = character.stashedGold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            EncumbranceDetailCard(
                                character = character,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
