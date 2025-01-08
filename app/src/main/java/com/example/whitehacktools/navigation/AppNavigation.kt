package com.example.whitehacktools.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whitehacktools.data.CharacterStore
import com.example.whitehacktools.model.*
import com.example.whitehacktools.ui.models.CharacterTab
import com.example.whitehacktools.ui.screens.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen(val route: String) {
    object CharacterList : Screen("characters")
    object CharacterDetail : Screen("character/{characterId}?tab={tab}") {
        fun createRoute(characterId: String, tab: CharacterTab = CharacterTab.Info) =
            "character/$characterId?tab=${tab.name}"
    }
    object CharacterForm : Screen("character/{characterId}/edit?tab={tab}") {
        fun createRoute(characterId: String, tab: CharacterTab = CharacterTab.Info) =
            "character/$characterId/edit?tab=${tab.name}"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    characterStore: CharacterStore
) {
    val characters = characterStore.characters.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList.route
    ) {
        composable(Screen.CharacterList.route) {
            CharacterListScreen(
                characters = characters.value,
                onSelectCharacter = { character ->
                    navController.navigate(Screen.CharacterDetail.createRoute(character.id))
                },
                onAddCharacter = {
                    navController.navigate(Screen.CharacterForm.createRoute("new"))
                },
                onDeleteCharacter = { character ->
                    scope.launch {
                        characterStore.saveCharacters(characters.value.filter { it.id != character.id })
                    }
                },
                characterStore = characterStore
            )
        }

        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType },
                navArgument("tab") {
                    type = NavType.StringType
                    defaultValue = CharacterTab.Info.name
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: return@composable
            val tabName = backStackEntry.arguments?.getString("tab") ?: CharacterTab.Info.name
            val selectedTab = CharacterTab.valueOf(tabName)
            val character = characters.value.find { it.id == characterId } ?: return@composable

            CharacterDetailScreen(
                character = character,
                initialTab = selectedTab,
                onNavigateBack = {
                    navController.navigate(Screen.CharacterList.route) {
                        popUpTo(Screen.CharacterList.route) { inclusive = true }
                    }
                },
                onEdit = { tab ->
                    navController.navigate(Screen.CharacterForm.createRoute(character.id, tab))
                }
            )
        }

        composable(
            route = Screen.CharacterForm.route,
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType },
                navArgument("tab") {
                    type = NavType.StringType
                    defaultValue = CharacterTab.Info.name
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: return@composable
            val tabName = backStackEntry.arguments?.getString("tab") ?: CharacterTab.Info.name
            val selectedTab = CharacterTab.valueOf(tabName)
            navController.previousBackStackEntry?.savedStateHandle?.get<PlayerCharacter>("character")?.let { character ->
                CharacterFormScreen(
                    id = character.id,
                    initialName = character.name,
                    initialPlayerName = character.playerName,
                    initialCharacterClass = character.characterClass,
                    initialLevel = character.level,
                    initialVocation = character.vocation,
                    initialSpecies = character.species,
                    initialAffiliations = character.affiliations,
                    initialLanguages = character.languages,
                    initialUseDefaultAttributes = character.useDefaultAttributes,
                    initialStrength = character.strength,
                    initialAgility = character.agility,
                    initialToughness = character.toughness,
                    initialIntelligence = character.intelligence,
                    initialWillpower = character.willpower,
                    initialCharisma = character.charisma,
                    initialCurrentHP = character.currentHP,
                    initialMaxHP = character.maxHP,
                    initialMovement = character.movement,
                    initialSaveColor = character.saveColor,
                    initialCoinsOnHand = character.coinsOnHand,
                    initialStashedCoins = character.stashedCoins,
                    initialExperience = character.experience,
                    initialCorruption = character.corruption,
                    initialNotes = character.notes,
                    initialAttributeGroupPairs = character.attributeGroupPairs ?: emptyList(),
                    initialAttunementSlots = character.attunementSlots ?: emptyList(),
                    initialStrongCombatOptions = character.strongCombatOptions,
                    initialConflictLoot = character.conflictLoot,
                    initialWiseMiracleSlots = character.wiseMiracleSlots ?: emptyList(),
                    initialBraveQuirkOptions = character.braveQuirkOptions ?: BraveQuirkOptions(),
                    initialComebackDice = character.comebackDice,
                    initialHasUsedSayNo = character.hasUsedSayNo,
                    initialCleverKnackOptions = character.cleverKnackOptions,
                    initialFortunateOptions = character.fortunateOptions,
                    initialWeapons = character.weapons ?: emptyList(),
                    initialArmor = character.armor ?: emptyList(),
                    initialGear = character.gear ?: emptyList(),
                    initialTab = selectedTab,
                    onNavigateBack = { tab -> 
                        if (characterId == "new") {
                            navController.navigate(Screen.CharacterList.route) {
                                popUpTo(Screen.CharacterList.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.CharacterDetail.createRoute(characterId, tab)) {
                                popUpTo(Screen.CharacterList.route)
                            }
                        }
                    },
                    onSave = { newCharacterData, tab ->
                        scope.launch {
                            if (characterId != "new") {
                                // Update existing character
                                val updatedCharacters = characters.value.map { existingChar ->
                                    if (existingChar.id == characterId) newCharacterData.copy(id = characterId) else existingChar
                                }
                                characterStore.saveCharacters(updatedCharacters)
                                navController.navigate(Screen.CharacterDetail.createRoute(characterId, tab)) {
                                    popUpTo(Screen.CharacterList.route)
                                }
                            } else {
                                // Create new character
                                val newCharacter = newCharacterData.copy(id = UUID.randomUUID().toString())
                                characterStore.saveCharacters(characters.value + newCharacter)
                                navController.navigate(Screen.CharacterDetail.createRoute(newCharacter.id, tab)) {
                                    popUpTo(Screen.CharacterList.route)
                                }
                            }
                        }
                    }
                )
            } ?: run {
                val character = if (characterId != "new") characters.value.find { it.id == characterId } else null

                CharacterFormScreen(
                    id = character?.id ?: "",
                    initialName = character?.name ?: "",
                    initialPlayerName = character?.playerName ?: "",
                    initialCharacterClass = character?.characterClass ?: "Deft",
                    initialLevel = character?.level ?: 1,
                    initialVocation = character?.vocation ?: "",
                    initialSpecies = character?.species ?: "",
                    initialAffiliations = character?.affiliations ?: emptyList(),
                    initialLanguages = character?.languages ?: emptyList(),
                    initialUseDefaultAttributes = character?.useDefaultAttributes ?: true,
                    initialStrength = character?.strength ?: 10,
                    initialAgility = character?.agility ?: 10,
                    initialToughness = character?.toughness ?: 10,
                    initialIntelligence = character?.intelligence ?: 10,
                    initialWillpower = character?.willpower ?: 10,
                    initialCharisma = character?.charisma ?: 10,
                    initialCurrentHP = character?.currentHP ?: 10,
                    initialMaxHP = character?.maxHP ?: 10,
                    initialMovement = character?.movement ?: 30,
                    initialSaveColor = character?.saveColor ?: "",
                    initialCoinsOnHand = character?.coinsOnHand ?: 0,
                    initialStashedCoins = character?.stashedCoins ?: 0,
                    initialExperience = character?.experience ?: 0,
                    initialCorruption = character?.corruption ?: 0,
                    initialNotes = character?.notes ?: "",
                    initialAttributeGroupPairs = character?.attributeGroupPairs ?: emptyList(),
                    initialAttunementSlots = character?.attunementSlots ?: emptyList(),
                    initialStrongCombatOptions = character?.strongCombatOptions,
                    initialConflictLoot = character?.conflictLoot,
                    initialWiseMiracleSlots = character?.wiseMiracleSlots ?: emptyList(),
                    initialBraveQuirkOptions = character?.braveQuirkOptions ?: BraveQuirkOptions(),
                    initialComebackDice = character?.comebackDice ?: 0,
                    initialHasUsedSayNo = character?.hasUsedSayNo ?: false,
                    initialCleverKnackOptions = character?.cleverKnackOptions ?: CleverKnackOptions(),
                    initialFortunateOptions = character?.fortunateOptions ?: FortunateOptions(),
                    initialWeapons = character?.weapons ?: emptyList(),
                    initialArmor = character?.armor ?: emptyList(),
                    initialGear = character?.gear ?: emptyList(),
                    initialTab = selectedTab,
                    onNavigateBack = { tab -> 
                        if (characterId == "new") {
                            navController.navigate(Screen.CharacterList.route) {
                                popUpTo(Screen.CharacterList.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.CharacterDetail.createRoute(characterId, tab)) {
                                popUpTo(Screen.CharacterList.route)
                            }
                        }
                    },
                    onSave = { newCharacterData, tab ->
                        scope.launch {
                            if (characterId != "new") {
                                // Update existing character
                                val updatedCharacters = characters.value.map { existingChar ->
                                    if (existingChar.id == characterId) newCharacterData.copy(id = characterId) else existingChar
                                }
                                characterStore.saveCharacters(updatedCharacters)
                                navController.navigate(Screen.CharacterDetail.createRoute(characterId, tab)) {
                                    popUpTo(Screen.CharacterList.route)
                                }
                            } else {
                                // Create new character
                                val newCharacter = newCharacterData.copy(id = UUID.randomUUID().toString())
                                characterStore.saveCharacters(characters.value + newCharacter)
                                navController.navigate(Screen.CharacterDetail.createRoute(newCharacter.id, tab)) {
                                    popUpTo(Screen.CharacterList.route)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
