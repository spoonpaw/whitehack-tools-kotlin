package com.example.whitehacktools.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    }
}
