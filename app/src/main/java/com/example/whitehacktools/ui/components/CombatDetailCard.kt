package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.utilities.AdvancementTables
import android.util.Log

@Composable
fun CombatDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    Log.d("CombatDetailCard", "Character Class: ${character.characterClass}")
    Log.d("CombatDetailCard", "Character Level: ${character.level}")
    
    val stats = AdvancementTables.stats(character.characterClass, character.level)
    Log.d("CombatDetailCard", "Attack Value from Tables: ${stats.attackValue}")
    
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
            value = stats.attackValue.toString()
        )
        
        DetailItem(
            label = "Defense Value",
            value = character.armor
                .filter { !it.isStashed }
                .sumOf { it.df }
                .toString()
        )
        
        DetailItem(
            label = "Movement",
            value = character.movement.toString()
        )

        DetailItem(
            label = "Initiative Bonus",
            value = "+${character.calculateInitiativeBonus()}"
        )
        
        DetailItem(
            label = "Save Color",
            value = character.saveColor
        )
    }
}
