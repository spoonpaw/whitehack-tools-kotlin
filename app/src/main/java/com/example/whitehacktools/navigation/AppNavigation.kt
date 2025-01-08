package com.example.whitehacktools.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whitehacktools.data.CharacterStore
import com.example.whitehacktools.model.AttributeArray
import com.example.whitehacktools.model.AttributeGroupPair
import com.example.whitehacktools.model.BraveAbilities
import com.example.whitehacktools.model.CleverAbilities
import com.example.whitehacktools.model.FortunateOptions
import com.example.whitehacktools.model.Gear
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.model.WiseMiracles
import com.example.whitehacktools.ui.models.CharacterTab
import com.example.whitehacktools.ui.screens.CharacterDetailScreen
import com.example.whitehacktools.ui.screens.CharacterFormScreen
import com.example.whitehacktools.ui.screens.CharacterListScreen
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
                        characterStore.saveCharacters(characters.value - character)
                    }
                }
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
            val character = if (characterId != "new") characters.value.find { it.id == characterId } else null

            CharacterFormScreen(
                initialName = character?.name ?: "",
                initialPlayerName = character?.playerName ?: "",
                initialLevel = character?.level ?: 1,
                initialCharacterClass = character?.characterClass ?: "Deft",
                initialVocation = character?.vocation ?: "",
                initialSpecies = character?.species ?: "",
                initialAffiliations = character?.affiliations ?: emptyList(),
                initialLanguages = character?.languages ?: emptyList(),
                initialCurrentHP = character?.currentHP ?: 10,
                initialMaxHP = character?.maxHP ?: 10,
                initialMovement = character?.movement ?: 30,
                initialSaveColor = character?.saveColor ?: "",
                initialGoldOnHand = character?.goldOnHand ?: 0,
                initialStashedGold = character?.stashedGold ?: 0,
                initialStrength = character?.strength ?: 10,
                initialAgility = character?.agility ?: 10,
                initialToughness = character?.toughness ?: 10,
                initialIntelligence = character?.intelligence ?: 10,
                initialWillpower = character?.willpower ?: 10,
                initialCharisma = character?.charisma ?: 10,
                initialUseDefaultAttributes = character?.useDefaultAttributes ?: true,
                initialCustomAttributeArray = character?.customAttributeArray,
                initialAttributeGroupPairs = character?.attributeGroupPairs ?: emptyList(),
                initialAttunementSlots = character?.attunementSlots ?: emptyList(),
                initialStrongCombatOptions = character?.strongCombatOptions,
                initialConflictLoot = character?.conflictLoot,
                initialWiseMiracles = character?.wiseMiracles ?: WiseMiracles(),
                initialBraveAbilities = character?.braveAbilities ?: BraveAbilities(),
                initialCleverAbilities = character?.cleverAbilities ?: CleverAbilities(),
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
