package com.example.whitehacktools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.ui.components.*
import com.example.whitehacktools.model.*
import com.example.whitehacktools.utilities.AdvancementTables
import com.example.whitehacktools.ui.models.CharacterTab

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
    id: String = "",
    initialName: String,
    initialPlayerName: String,
    initialCharacterClass: String,
    initialLevel: Int,
    initialVocation: String,
    initialSpecies: String,
    initialAffiliations: List<String>,
    initialLanguages: List<String>,
    initialUseDefaultAttributes: Boolean,
    initialStrength: Int,
    initialAgility: Int,
    initialToughness: Int,
    initialIntelligence: Int,
    initialWillpower: Int,
    initialCharisma: Int,
    initialCurrentHP: Int,
    initialMaxHP: Int,
    initialMovement: Int,
    initialSaveColor: String,
    initialCoinsOnHand: Int,
    initialStashedCoins: Int,
    initialExperience: Int,
    initialCorruption: Int,
    initialNotes: String,
    initialCustomAttributeArray: AttributeArray? = null,
    initialAttributeGroupPairs: List<AttributeGroupPair> = emptyList(),
    initialAttunementSlots: List<AttunementSlot> = emptyList(),
    initialStrongCombatOptions: StrongCombatOptions? = null,
    initialConflictLoot: ConflictLoot? = null,
    initialWiseMiracleSlots: List<WiseMiracleSlot> = emptyList(),
    initialBraveAbilities: BraveAbilities = BraveAbilities(),
    initialCleverAbilities: CleverAbilities = CleverAbilities(),
    initialFortunateOptions: FortunateOptions = FortunateOptions(),
    initialWeapons: List<Weapon> = emptyList(),
    initialArmor: List<Armor> = emptyList(),
    initialGear: List<Gear> = emptyList(),
    initialTab: CharacterTab = CharacterTab.Info,
    onNavigateBack: (CharacterTab) -> Unit = {},
    onSave: (PlayerCharacter, CharacterTab) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var playerName by remember { mutableStateOf(initialPlayerName) }
    var level by remember { mutableStateOf(initialLevel.toString()) }
    var characterClass by remember { mutableStateOf(initialCharacterClass) }
    var vocation by remember { mutableStateOf(initialVocation) }
    var species by remember { mutableStateOf(initialSpecies) }
    var affiliations by remember { mutableStateOf(initialAffiliations) }
    var languages by remember { mutableStateOf(initialLanguages) }
    var selectedTab by remember { mutableStateOf(initialTab) }
    var weapons by remember { mutableStateOf(initialWeapons) }
    var armor by remember { mutableStateOf(initialArmor) }
    var gear by remember { mutableStateOf(initialGear) }
    val scrollState = rememberScrollState()

    // Scroll to top when tab changes or when screen is shown with new character
    LaunchedEffect(selectedTab, initialName) {
        scrollState.scrollTo(0)
    }
    
    // Combat stats
    var currentHP by remember { mutableStateOf(initialCurrentHP.toString()) }
    var maxHP by remember { mutableStateOf(initialMaxHP.toString()) }
    var movement by remember { mutableStateOf(initialMovement.toString()) }
    var saveColor by remember { mutableStateOf(initialSaveColor) }
    var coinsOnHand by remember { mutableStateOf(initialCoinsOnHand.toString()) }
    var stashedCoins by remember { mutableStateOf(initialStashedCoins.toString()) }
    var experience by remember { mutableStateOf(initialExperience.toString()) }
    var corruption by remember { mutableStateOf(initialCorruption.toString()) }
    var notes by remember { mutableStateOf(initialNotes) }
    
    // Attributes
    var useDefaultAttributes by remember { mutableStateOf(initialUseDefaultAttributes) }
    var strength by remember { mutableStateOf(initialStrength.toString()) }
    var agility by remember { mutableStateOf(initialAgility.toString()) }
    var toughness by remember { mutableStateOf(initialToughness.toString()) }
    var intelligence by remember { mutableStateOf(initialIntelligence.toString()) }
    var willpower by remember { mutableStateOf(initialWillpower.toString()) }
    var charisma by remember { mutableStateOf(initialCharisma.toString()) }
    var customAttributeArray by remember { mutableStateOf(initialCustomAttributeArray) }
    var attributeGroupPairs by remember { mutableStateOf(initialAttributeGroupPairs) }
    var attunementSlots by remember { mutableStateOf(initialAttunementSlots) }

    // Strong Features
    var strongCombatOptions by remember { mutableStateOf(initialStrongCombatOptions ?: StrongCombatOptions()) }
    var conflictLoot by remember { mutableStateOf(initialConflictLoot) }

    // Wise Features
    var wiseMiracleSlots by remember { mutableStateOf(initialWiseMiracleSlots) }

    // Brave Features
    var braveAbilities by remember { mutableStateOf(initialBraveAbilities) }

    // Clever Features
    var cleverAbilities by remember { mutableStateOf(initialCleverAbilities) }

    // Fortunate Features
    var fortunateOptions by remember { mutableStateOf(initialFortunateOptions) }

    // Create a temporary character for class-specific forms
    var tempCharacter by remember { mutableStateOf(
        PlayerCharacter(
            id = id,
            name = name,
            playerName = playerName,
            characterClass = characterClass,
            level = level.toIntOrNull() ?: 1,
            vocation = vocation,
            species = species,
            affiliations = affiliations,
            languages = languages,
            currentHP = currentHP.toIntOrNull() ?: 10,
            maxHP = maxHP.toIntOrNull() ?: 10,
            movement = movement.toIntOrNull() ?: 30,
            saveColor = saveColor,
            coinsOnHand = coinsOnHand.toIntOrNull() ?: 0,
            stashedCoins = stashedCoins.toIntOrNull() ?: 0,
            experience = experience.toIntOrNull() ?: 0,
            corruption = corruption.toIntOrNull() ?: 0,
            notes = notes,
            useDefaultAttributes = useDefaultAttributes,
            strength = strength.toIntOrNull() ?: 10,
            agility = agility.toIntOrNull() ?: 10,
            toughness = toughness.toIntOrNull() ?: 10,
            intelligence = intelligence.toIntOrNull() ?: 10,
            willpower = willpower.toIntOrNull() ?: 10,
            charisma = charisma.toIntOrNull() ?: 10,
            customAttributeArray = customAttributeArray,
            attributeGroupPairs = attributeGroupPairs,
            attunementSlots = attunementSlots,
            strongCombatOptions = strongCombatOptions,
            conflictLoot = conflictLoot,
            wiseMiracleSlots = wiseMiracleSlots,
            braveAbilities = braveAbilities,
            cleverAbilities = cleverAbilities,
            fortunateOptions = fortunateOptions,
            weapons = weapons,
            armor = armor,
            gear = gear
        )
    ) }

    // Update tempCharacter whenever any of its fields change
    LaunchedEffect(
        name, playerName, characterClass, level, vocation, species,
        affiliations, languages, currentHP, maxHP, movement, saveColor,
        coinsOnHand, stashedCoins, experience, corruption, notes,
        useDefaultAttributes, strength, agility, toughness, intelligence,
        willpower, charisma, customAttributeArray, attributeGroupPairs, attunementSlots,
        strongCombatOptions, conflictLoot, wiseMiracleSlots, braveAbilities, cleverAbilities,
        fortunateOptions, weapons, armor, gear
    ) {
        tempCharacter = PlayerCharacter(
            id = id,
            name = name,
            playerName = playerName,
            characterClass = characterClass,
            level = level.toIntOrNull() ?: 1,
            vocation = vocation,
            species = species,
            affiliations = affiliations,
            languages = languages,
            currentHP = currentHP.toIntOrNull() ?: 10,
            maxHP = maxHP.toIntOrNull() ?: 10,
            movement = movement.toIntOrNull() ?: 30,
            saveColor = saveColor,
            coinsOnHand = coinsOnHand.toIntOrNull() ?: 0,
            stashedCoins = stashedCoins.toIntOrNull() ?: 0,
            experience = experience.toIntOrNull() ?: 0,
            corruption = corruption.toIntOrNull() ?: 0,
            notes = notes,
            useDefaultAttributes = useDefaultAttributes,
            strength = strength.toIntOrNull() ?: 10,
            agility = agility.toIntOrNull() ?: 10,
            toughness = toughness.toIntOrNull() ?: 10,
            intelligence = intelligence.toIntOrNull() ?: 10,
            willpower = willpower.toIntOrNull() ?: 10,
            charisma = charisma.toIntOrNull() ?: 10,
            customAttributeArray = customAttributeArray,
            attributeGroupPairs = attributeGroupPairs,
            attunementSlots = attunementSlots,
            strongCombatOptions = strongCombatOptions,
            conflictLoot = conflictLoot,
            wiseMiracleSlots = wiseMiracleSlots,
            braveAbilities = braveAbilities,
            cleverAbilities = cleverAbilities,
            fortunateOptions = fortunateOptions,
            weapons = weapons,
            armor = armor,
            gear = gear
        )
    }

    DisposableEffect(selectedTab) {
        onDispose { }
    }

    Scaffold(
        topBar = {
            WhitehackTopAppBar(
                title = if (initialName.isEmpty()) "New Character" else "Edit Character",
                onNavigateBack = { onNavigateBack(selectedTab) },
                actions = listOf(
                    TopBarAction.TextAction(
                        text = "Save",
                        onClick = {
                            onSave(
                                PlayerCharacter(
                                    id = id,
                                    name = name,
                                    playerName = playerName,
                                    characterClass = characterClass,
                                    level = level.toIntOrNull() ?: 1,
                                    vocation = vocation,
                                    species = species,
                                    affiliations = affiliations,
                                    languages = languages,
                                    currentHP = currentHP.toIntOrNull() ?: 10,
                                    maxHP = maxHP.toIntOrNull() ?: 10,
                                    movement = movement.toIntOrNull() ?: 30,
                                    saveColor = saveColor,
                                    coinsOnHand = coinsOnHand.toIntOrNull() ?: 0,
                                    stashedCoins = stashedCoins.toIntOrNull() ?: 0,
                                    experience = experience.toIntOrNull() ?: 0,
                                    corruption = corruption.toIntOrNull() ?: 0,
                                    notes = notes,
                                    useDefaultAttributes = useDefaultAttributes,
                                    strength = strength.toIntOrNull() ?: 10,
                                    agility = agility.toIntOrNull() ?: 10,
                                    toughness = toughness.toIntOrNull() ?: 10,
                                    intelligence = intelligence.toIntOrNull() ?: 10,
                                    willpower = willpower.toIntOrNull() ?: 10,
                                    charisma = charisma.toIntOrNull() ?: 10,
                                    customAttributeArray = customAttributeArray,
                                    attributeGroupPairs = attributeGroupPairs,
                                    attunementSlots = tempCharacter.attunementSlots,
                                    strongCombatOptions = strongCombatOptions,
                                    conflictLoot = conflictLoot,
                                    wiseMiracleSlots = wiseMiracleSlots,
                                    braveAbilities = braveAbilities,
                                    cleverAbilities = cleverAbilities,
                                    fortunateOptions = fortunateOptions.updateForLevel(level.toIntOrNull() ?: 1),
                                    weapons = weapons,
                                    armor = armor,
                                    gear = gear
                                ),
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
                    .verticalScroll(scrollState)
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
                            attributeGroupPairs = attributeGroupPairs,
                            onAttributeGroupPairsChange = { attributeGroupPairs = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GroupsFormCard(
                            vocation = tempCharacter.effectiveVocation ?: "",
                            onVocationChange = { vocation = it },
                            species = tempCharacter.effectiveSpecies ?: "",
                            onSpeciesChange = { species = it },
                            affiliations = tempCharacter.effectiveAffiliations ?: emptyList(),
                            onAffiliationsChange = { affiliations = it },
                            attributeGroupPairs = attributeGroupPairs,
                            onAttributeGroupPairsChange = { attributeGroupPairs = it },
                            availableAttributes = if (useDefaultAttributes) {
                                PlayerCharacter.DEFAULT_ATTRIBUTES
                            } else {
                                customAttributeArray?.attributes?.keys?.toList() ?: emptyList()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LanguagesFormCard(
                            languages = languages,
                            onLanguagesChange = { languages = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        when (characterClass) {
                            "Deft" -> DeftFormCard(
                                character = tempCharacter,
                                onCharacterChange = { updatedCharacter ->
                                    tempCharacter = updatedCharacter
                                    attunementSlots = updatedCharacter.attunementSlots
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            "Strong" -> StrongFormCard(
                                characterClass = characterClass,
                                level = level.toIntOrNull() ?: 1,
                                strongCombatOptions = strongCombatOptions,
                                currentConflictLoot = conflictLoot,
                                onStrongCombatOptionsChanged = { strongCombatOptions = it },
                                onConflictLootChanged = { conflictLoot = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            "Wise" -> WiseFormCard(
                                characterClass = characterClass,
                                level = level.toIntOrNull() ?: 1,
                                willpower = willpower.toIntOrNull() ?: 10,
                                useCustomAttributes = !useDefaultAttributes,
                                wiseMiracleSlots = wiseMiracleSlots,
                                onWiseMiracleSlotsChanged = { wiseMiracleSlots = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            "Brave" -> BraveFormCard(
                                characterClass = characterClass,
                                level = level.toIntOrNull() ?: 1,
                                braveAbilities = braveAbilities,
                                onBraveAbilitiesChanged = { braveAbilities = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            "Clever" -> CleverFormCard(
                                characterClass = characterClass,
                                level = level.toIntOrNull() ?: 1,
                                cleverAbilities = cleverAbilities,
                                onCleverAbilitiesChanged = { cleverAbilities = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            "Fortunate" -> FortunateFormCard(
                                level = level.toIntOrNull() ?: 1,
                                fortunateOptions = fortunateOptions,
                                onFortunateOptionsChanged = { fortunateOptions = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AdditionalInfoFormCard(
                            experience = experience,
                            onExperienceChange = { experience = it },
                            corruption = corruption,
                            onCorruptionChange = { corruption = it },
                            notes = notes,
                            onNotesChange = { notes = it },
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
                        WeaponFormCard(
                            weapons = weapons,
                            onWeaponsChange = { weapons = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ArmorFormCard(
                            armor = armor,
                            onArmorChange = { updatedArmor ->
                                armor = updatedArmor
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        EquipmentFormCard(
                            gear = gear,
                            onGearChange = { gear = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GoldFormCard(
                            coinsOnHand = coinsOnHand,
                            onCoinsOnHandChange = { coinsOnHand = it },
                            stashedCoins = stashedCoins,
                            onStashedCoinsChange = { stashedCoins = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
