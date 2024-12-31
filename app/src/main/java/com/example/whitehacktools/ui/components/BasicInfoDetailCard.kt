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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItem(
                label = "Character Name",
                value = character.name.ifEmpty { "Unnamed" }
            )
            
            DetailItem(
                label = "Player Name",
                value = character.playerName.ifEmpty { "Unknown" }
            )
            
            DetailItem(
                label = "Character Class",
                value = character.characterClass.ifEmpty { "Unspecified" }
            )
            
            DetailItem(
                label = "Level",
                value = if (character.level > 0) character.level.toString() else "1"
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
                color = if (value in listOf("Unnamed", "Unknown", "Unspecified")) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}
