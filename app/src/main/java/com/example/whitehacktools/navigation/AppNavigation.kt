package com.example.whitehacktools.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.screens.CharacterFormScreen
import com.example.whitehacktools.ui.screens.CharacterListScreen
import com.example.whitehacktools.ui.screens.CharacterDetailScreen
import java.util.UUID

sealed class Screen(val route: String) {
    object CharacterList : Screen("characterList")
    object CharacterForm : Screen("characterForm?characterId={characterId}") {
        fun createRoute(characterId: String? = null) = 
            if (characterId != null) "characterForm?characterId=$characterId"
            else "characterForm?characterId"
    }
    object CharacterDetail : Screen("characterDetail/{characterId}") {
        fun createRoute(characterId: String) = "characterDetail/$characterId"
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var characters by remember { mutableStateOf(listOf<PlayerCharacter>()) }
    
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
                onImportCharacter = {
                    // TODO: Handle import
                },
                onExportCharacter = {
                    // TODO: Handle export
                },
                onDeleteCharacter = { character ->
                    characters = characters.filter { it.id != character.id }
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
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            val character = characterId?.let { id -> characters.find { it.id == id } }
            
            CharacterFormScreen(
                initialName = character?.name ?: "",
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSave = { name ->
                    if (character != null) {
                        // Update existing character
                        characters = characters.map { 
                            if (it.id == character.id) it.copy(name = name)
                            else it
                        }
                    } else {
                        // Create new character
                        val newCharacter = PlayerCharacter(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            characterClass = "Fighter", // Default values for now
                            level = 1
                        )
                        characters = characters + newCharacter
                    }
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            val character = characters.find { it.id == characterId }
            
            if (character != null) {
                CharacterDetailScreen(
                    character = character,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onEditCharacter = {
                        navController.navigate(Screen.CharacterForm.createRoute(character.id))
                    }
                )
            }
        }
    }
}
