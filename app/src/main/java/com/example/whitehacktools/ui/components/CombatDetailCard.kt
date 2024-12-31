package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // First row: HP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatBox(
                    title = "Current HP",
                    value = character.currentHP.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatBox(
                    title = "Max HP",
                    value = character.maxHP.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            // Second row: Attack and Defense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatBox(
                    title = "Attack",
                    value = character.attackValue.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatBox(
                    title = "Defense",
                    value = character.defenseValue.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            // Third row: Movement and Initiative
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatBox(
                    title = "Movement",
                    value = "${character.movement} ft",
                    modifier = Modifier.weight(1f)
                )
                StatBox(
                    title = "Initiative",
                    value = if (character.initiativeBonus > 0) "+${character.initiativeBonus}" else "0",
                    modifier = Modifier.weight(1f)
                )
            }

            // Fourth row: Save Color
            SaveColorBox(
                title = "Save Color",
                value = character.saveColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun SaveColorBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
