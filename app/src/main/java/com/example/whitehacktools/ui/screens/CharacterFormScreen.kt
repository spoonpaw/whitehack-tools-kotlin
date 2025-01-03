package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.ui.components.*
import com.example.whitehacktools.ui.models.CharacterTab
import com.example.whitehacktools.model.AttributeArray

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
    initialVocation: String = "",
    initialSpecies: String = "",
    initialCurrentHP: Int = 10,
    initialMaxHP: Int = 10,
    initialMovement: Int = 30,
    initialSaveColor: String = "",
    initialGoldOnHand: Int = 0,
    initialStashedGold: Int = 0,
    initialStrength: Int = 10,
    initialAgility: Int = 10,
    initialToughness: Int = 10,
    initialIntelligence: Int = 10,
    initialWillpower: Int = 10,
    initialCharisma: Int = 10,
    initialUseDefaultAttributes: Boolean = true,
    initialCustomAttributeArray: AttributeArray? = null,
    initialTab: CharacterTab = CharacterTab.Info,
    onNavigateBack: () -> Unit = {},
    onSave: (
        name: String,
        level: Int,
        characterClass: String,
        vocation: String,
        species: String,
        currentHP: Int,
        maxHP: Int,
        movement: Int,
        saveColor: String,
        goldOnHand: String,
        stashedGold: String,
        useDefaultAttributes: Boolean,
        strength: String,
        agility: String,
        toughness: String,
        intelligence: String,
        willpower: String,
        charisma: String,
        customAttributeArray: AttributeArray?,
        tab: CharacterTab
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var playerName by remember { mutableStateOf(initialPlayerName) }
    var level by remember { mutableStateOf(initialLevel.toString()) }
    var characterClass by remember { mutableStateOf(initialCharacterClass) }
    var vocation by remember { mutableStateOf(initialVocation) }
    var species by remember { mutableStateOf(initialSpecies) }
    var selectedTab by remember { mutableStateOf(initialTab) }
    
    // Combat stats
    var currentHP by remember { mutableStateOf(initialCurrentHP.toString()) }
    var maxHP by remember { mutableStateOf(initialMaxHP.toString()) }
    var movement by remember { mutableStateOf(initialMovement.toString()) }
    var saveColor by remember { mutableStateOf(initialSaveColor) }
    var goldOnHand by remember { mutableStateOf(initialGoldOnHand.toString()) }
    var stashedGold by remember { mutableStateOf(initialStashedGold.toString()) }
    
    // Attributes
    var useDefaultAttributes by remember { mutableStateOf(initialUseDefaultAttributes) }
    var strength by remember { mutableStateOf(initialStrength.toString()) }
    var agility by remember { mutableStateOf(initialAgility.toString()) }
    var toughness by remember { mutableStateOf(initialToughness.toString()) }
    var intelligence by remember { mutableStateOf(initialIntelligence.toString()) }
    var willpower by remember { mutableStateOf(initialWillpower.toString()) }
    var charisma by remember { mutableStateOf(initialCharisma.toString()) }
    var customAttributeArray by remember { mutableStateOf(initialCustomAttributeArray) }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = if (initialName.isEmpty()) "New Character" else "Edit Character",
                onNavigateBack = onNavigateBack,
                actions = listOf(
                    TopBarAction.TextAction(
                        text = "Save",
                        onClick = {
                            onSave(
                                name,
                                level.toIntOrNull() ?: 1,
                                characterClass,
                                vocation,
                                species,
                                currentHP.toIntOrNull() ?: 10,
                                maxHP.toIntOrNull() ?: 10,
                                movement.toIntOrNull() ?: 30,
                                saveColor,
                                goldOnHand,
                                stashedGold,
                                useDefaultAttributes,
                                strength,
                                agility,
                                toughness,
                                intelligence,
                                willpower,
                                charisma,
                                customAttributeArray,
                                selectedTab
                            )
                        },
                        enabled = name.isNotBlank() && level.toIntOrNull() in 1..10
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
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    CharacterTab.Info -> {
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
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AttributesFormCard(
                            useDefaultAttributes = useDefaultAttributes,
                            onUseDefaultAttributesChange = { useDefaultAttributes = it },
                            strength = strength,
                            onStrengthChange = { strength = it },
                            agility = agility,
                            onAgilityChange = { agility = it },
                            toughness = toughness,
                            onToughnessChange = { toughness = it },
                            intelligence = intelligence,
                            onIntelligenceChange = { intelligence = it },
                            willpower = willpower,
                            onWillpowerChange = { willpower = it },
                            charisma = charisma,
                            onCharismaChange = { charisma = it },
                            customAttributeArray = customAttributeArray,
                            onCustomAttributeArrayChange = { customAttributeArray = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GroupsFormCard(
                            vocation = vocation,
                            onVocationChange = { vocation = it },
                            species = species,
                            onSpeciesChange = { species = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CharacterTab.Combat -> {
                        CombatFormCard(
                            currentHP = currentHP,
                            onCurrentHPChange = { currentHP = it },
                            maxHP = maxHP,
                            onMaxHPChange = { maxHP = it },
                            movement = movement,
                            onMovementChange = { movement = it },
                            saveColor = saveColor,
                            onSaveColorChange = { saveColor = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CharacterTab.Equipment -> {
                        GoldFormCard(
                            goldOnHand = goldOnHand,
                            onGoldOnHandChange = { goldOnHand = it },
                            stashedGold = stashedGold,
                            onStashedGoldChange = { stashedGold = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
