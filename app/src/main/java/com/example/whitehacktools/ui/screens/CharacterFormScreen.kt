package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.ui.components.*

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
    initialCurrentHP: Int = 10,
    initialMaxHP: Int = 10,
    initialAttackValue: Int = 0,
    initialDefenseValue: Int = 0,
    initialMovement: Int = 30,
    initialInitiativeBonus: Int = 0,
    initialSaveColor: String = "",
    initialTab: CharacterTab = CharacterTab.Info,
    onNavigateBack: () -> Unit = {},
    onSave: (
        name: String,
        level: Int,
        characterClass: String,
        currentHP: Int,
        maxHP: Int,
        attackValue: Int,
        defenseValue: Int,
        movement: Int,
        initiativeBonus: Int,
        saveColor: String,
        selectedTab: CharacterTab
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var playerName by remember { mutableStateOf(initialPlayerName) }
    var level by remember { mutableStateOf(initialLevel.toString()) }
    var characterClass by remember { mutableStateOf(initialCharacterClass) }
    var selectedTab by remember { mutableStateOf(initialTab) }
    
    // Combat stats
    var currentHP by remember { mutableStateOf(initialCurrentHP.toString()) }
    var maxHP by remember { mutableStateOf(initialMaxHP.toString()) }
    var attackValue by remember { mutableStateOf(initialAttackValue.toString()) }
    var defenseValue by remember { mutableStateOf(initialDefenseValue.toString()) }
    var movement by remember { mutableStateOf(initialMovement.toString()) }
    var initiativeBonus by remember { mutableStateOf(initialInitiativeBonus.toString()) }
    var saveColor by remember { mutableStateOf(initialSaveColor) }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = if (initialName.isEmpty()) "New Character" else "Edit Character",
                onNavigateBack = onNavigateBack,
                actions = listOf(
                    TopBarAction.TextAction(
                        text = "Save",
                        onClick = {
                            val levelInt = level.toIntOrNull() ?: 1
                            val currentHPInt = currentHP.toIntOrNull() ?: 10
                            val maxHPInt = maxHP.toIntOrNull() ?: 10
                            val attackInt = attackValue.toIntOrNull() ?: 0
                            val defenseInt = defenseValue.toIntOrNull() ?: 0
                            val movementInt = movement.toIntOrNull() ?: 30
                            val initiativeInt = initiativeBonus.toIntOrNull() ?: 0
                            
                            onSave(
                                name,
                                levelInt,
                                characterClass,
                                currentHPInt,
                                maxHPInt,
                                attackInt,
                                defenseInt,
                                movementInt,
                                initiativeInt,
                                saveColor,
                                selectedTab
                            )
                        },
                        enabled = name.isNotBlank() && level.toIntOrNull() in 1..10,
                        isPrimary = true
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
                CharacterTab.Info -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                CharacterTab.Combat -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CombatFormCard(
                            currentHP = currentHP,
                            onCurrentHPChange = { currentHP = it },
                            maxHP = maxHP,
                            onMaxHPChange = { maxHP = it },
                            attackValue = attackValue,
                            onAttackValueChange = { attackValue = it },
                            defenseValue = defenseValue,
                            onDefenseValueChange = { defenseValue = it },
                            movement = movement,
                            onMovementChange = { movement = it },
                            initiativeBonus = initiativeBonus,
                            onInitiativeBonusChange = { initiativeBonus = it },
                            saveColor = saveColor,
                            onSaveColorChange = { saveColor = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                CharacterTab.Equipment -> {
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
                                text = "Equipment form coming soon...",
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
