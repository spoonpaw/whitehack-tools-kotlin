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
        title = "Combat Stats",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItem(
                label = "Current HP",
                value = character.currentHP.toString()
            )
            
            DetailItem(
                label = "Maximum HP",
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
                value = "${character.movement} ft"
            )
            
            DetailItem(
                label = "Initiative Bonus",
                value = if (character.initiativeBonus >= 0) "+${character.initiativeBonus}" else character.initiativeBonus.toString()
            )
            
            DetailItem(
                label = "Save Color",
                value = character.saveColor.ifEmpty { "Unspecified" }
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = if (value == "Unspecified") {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}
