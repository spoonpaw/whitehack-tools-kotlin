package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter

@Composable
fun CombatDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Combat",
        modifier = modifier
    ) {
        DetailItem(
            label = "Current HP",
            value = character.currentHP.toString()
        )
        
        DetailItem(
            label = "Max HP",
            value = character.maxHP.toString()
        )
        
        DetailItem(
            label = "Attack Value",
            value = character.attackValue.toString()
        )
        
        DetailItem(
            label = "Defense Value",
            value = character.defenseValue.toString()
        )
        
        DetailItem(
            label = "Movement",
            value = character.movement.toString()
        )
        
        DetailItem(
            label = "Initiative Bonus",
            value = character.initiativeBonus.toString()
        )
        
        DetailItem(
            label = "Save Color",
            value = character.saveColor
        )
    }
}
