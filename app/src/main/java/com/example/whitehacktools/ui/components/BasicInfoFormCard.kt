package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoFormCard(
    name: String,
    onNameChange: (String) -> Unit,
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
    level: String,
    onLevelChange: (String) -> Unit,
    characterClass: String,
    onCharacterClassChange: (String) -> Unit,
    characterClasses: List<String>,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Basic Info",
        modifier = modifier
    ) {
        FormField(
            value = name,
            onValueChange = onNameChange,
            label = "Character Name"
        )
        
        FormField(
            value = playerName,
            onValueChange = onPlayerNameChange,
            label = "Player Name"
        )
        
        FormField(
            value = level,
            onValueChange = onLevelChange,
            label = "Level (1-10)",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            isError = level.toIntOrNull() !in 1..10,
            validate = { it.isEmpty() || it.toIntOrNull() in 1..10 }
        )
        
        DropdownField(
            value = characterClass,
            onValueChange = onCharacterClassChange,
            label = "Character Class",
            options = characterClasses
        )
    }
}
