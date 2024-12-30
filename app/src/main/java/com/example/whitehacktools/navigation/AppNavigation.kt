package com.example.whitehacktools.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.ui.screens.CharacterFormScreen
import com.example.whitehacktools.ui.screens.CharacterListScreen
import java.util.UUID

sealed class Screen(val route: String) {
    object CharacterList : Screen("characterList")
    object CharacterForm : Screen("characterForm")
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
                    navController.navigate(Screen.CharacterForm.route)
                },
                onImportCharacter = {
                    // TODO: Handle import
                },
                onExportCharacter = {
                    // TODO: Handle export
                }
            )
        }
        
        composable(Screen.CharacterForm.route) {
            CharacterFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSave = { name ->
                    val newCharacter = PlayerCharacter(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        characterClass = "Fighter", // Default values for now
                        level = 1
                    )
                    characters = characters + newCharacter
                }
            )
        }
    }
}
