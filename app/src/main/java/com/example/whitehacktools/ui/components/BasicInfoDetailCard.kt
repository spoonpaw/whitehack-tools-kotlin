package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter

@Composable
fun BasicInfoDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Basic Info",
        modifier = modifier
    ) {
        DetailItem(
            label = "Name",
            value = character.name
        )
        
        DetailItem(
            label = "Level",
            value = character.level.toString()
        )
        
        DetailItem(
            label = "Character Class",
            value = character.characterClass
        )
        
        DetailItem(
            label = "Save Color",
            value = character.saveColor
        )
    }
}
