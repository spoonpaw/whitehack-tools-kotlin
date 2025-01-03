package com.example.whitehacktools.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whitehacktools.data.CharacterStore
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.screens.*
import com.example.whitehacktools.ui.components.CharacterTab
import kotlinx.coroutines.launch
import java.util.*

sealed class Screen(val route: String) {
    object CharacterList : Screen("characterList")
    object CharacterForm : Screen("characterForm/{characterId}/{selectedTab}") {
        fun createRoute(characterId: String? = null, selectedTab: CharacterTab = CharacterTab.Info) = 
            "characterForm/${characterId ?: "new"}/${selectedTab.name}"
    }
    object CharacterDetail : Screen("characterDetail/{characterId}/{selectedTab}") {
        fun createRoute(characterId: String, selectedTab: CharacterTab = CharacterTab.Info) = 
            "characterDetail/$characterId/${selectedTab.name}"
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val characterStore = remember { CharacterStore(context) }
    val characters by characterStore.characters.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList.route,
        modifier = modifier
    ) {
        composable(Screen.CharacterList.route) {
            CharacterListScreen(
                characters = characters,
                onAddCharacter = {
                    navController.navigate(Screen.CharacterForm.createRoute())
                },
                onSelectCharacter = { character ->
                    navController.navigate(Screen.CharacterDetail.createRoute(character.id))
                },
                onImportCharacter = {},  // Keeping these for now
                onExportCharacter = {},  // Keeping these for now
                onDeleteCharacter = { character ->
                    scope.launch {
                        characterStore.saveCharacters(characters.filter { it.id != character.id })
                    }
                }
            )
        }
        
        composable(
            route = Screen.CharacterForm.route,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("selectedTab") {
                    type = NavType.StringType
                    defaultValue = CharacterTab.Info.name
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            val selectedTabName = backStackEntry.arguments?.getString("selectedTab") ?: CharacterTab.Info.name
            val selectedTab = CharacterTab.valueOf(selectedTabName)
            val character = if (characterId != "new") characters.find { it.id == characterId } else null
            
            CharacterFormScreen(
                initialName = character?.name ?: "",
                initialPlayerName = character?.playerName ?: "",
                initialLevel = character?.level ?: 1,
                initialCharacterClass = character?.characterClass ?: "Deft",
                initialCurrentHP = character?.currentHP ?: 10,
                initialMaxHP = character?.maxHP ?: 10,
                initialMovement = character?.movement ?: 30,
                initialSaveColor = character?.saveColor ?: "",
                initialGoldOnHand = character?.goldOnHand ?: 0,
                initialStashedGold = character?.stashedGold ?: 0,
                initialTab = selectedTab,
                onNavigateBack = { tab ->
                    if (characterId == "new") {
                        // From new character form, go back to list
                        navController.navigate(Screen.CharacterList.route) {
                            popUpTo(Screen.CharacterList.route) { inclusive = true }
                        }
                    } else {
                        // From edit form, go back to detail
                        navController.navigate(Screen.CharacterDetail.createRoute(characterId!!, tab)) {
                            popUpTo(Screen.CharacterForm.route) { inclusive = true }
                        }
                    }
                },
                onSave = { name, level, characterClass, currentHP, maxHP, attackValue, defenseValue, movement, initiativeBonus, saveColor, goldOnHand, stashedGold, tab ->
                    scope.launch {
                        if (character != null) {
                            // Update existing character
                            val updatedCharacters = characters.map { existingChar -> 
                                if (existingChar.id == character.id) {
                                    existingChar.copy(
                                        name = name,
                                        level = level,
                                        characterClass = characterClass,
                                        currentHP = currentHP,
                                        maxHP = maxHP,
                                        attackValue = attackValue,
                                        defenseValue = defenseValue,
                                        movement = movement,
                                        initiativeBonus = initiativeBonus,
                                        saveColor = saveColor,
                                        goldOnHand = goldOnHand.toIntOrNull() ?: 0,
                                        stashedGold = stashedGold.toIntOrNull() ?: 0
                                    )
                                } else {
                                    existingChar
                                }
                            }
                            characterStore.saveCharacters(updatedCharacters)
                            navController.navigate(Screen.CharacterDetail.createRoute(character.id, tab)) {
                                popUpTo(Screen.CharacterForm.route) { inclusive = true }
                            }
                        } else {
                            // Create new character
                            val newCharacter = PlayerCharacter(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                playerName = "",
                                level = level,
                                characterClass = characterClass,
                                currentHP = currentHP,
                                maxHP = maxHP,
                                attackValue = attackValue,
                                defenseValue = defenseValue,
                                movement = movement,
                                initiativeBonus = initiativeBonus,
                                saveColor = saveColor,
                                goldOnHand = goldOnHand.toIntOrNull() ?: 0,
                                stashedGold = stashedGold.toIntOrNull() ?: 0
                            )
                            characterStore.saveCharacters(characters + newCharacter)
                            navController.navigate(Screen.CharacterDetail.createRoute(newCharacter.id, tab)) {
                                popUpTo(Screen.CharacterForm.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        
        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.StringType
                },
                navArgument("selectedTab") {
                    type = NavType.StringType
                    defaultValue = CharacterTab.Info.name
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")!!
            val selectedTabName = backStackEntry.arguments?.getString("selectedTab") ?: CharacterTab.Info.name
            val selectedTab = CharacterTab.valueOf(selectedTabName)
            val character = characters.find { it.id == characterId }
            
            if (character != null) {
                CharacterDetailScreen(
                    character = character,
                    onNavigateBack = {
                        navController.navigate(Screen.CharacterList.route) {
                            popUpTo(Screen.CharacterList.route) { inclusive = true }
                        }
                    },
                    onEditCharacter = { currentCharacter, currentTab ->
                        navController.navigate(Screen.CharacterForm.createRoute(currentCharacter.id, currentTab))
                    },
                    initialTab = selectedTab
                )
            }
        }
    }
}
