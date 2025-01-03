package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.whitehacktools.model.PlayerCharacter

@Composable
fun GroupsDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Groups",
        modifier = modifier
    ) {
        DetailItem(
            label = "Vocation",
            value = if (character.vocation.isBlank()) "Not specified" else character.vocation,
            valueColor = if (character.vocation.isBlank()) 
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            else
                MaterialTheme.colorScheme.onSurface
        )
        DetailItem(
            label = "Species",
            value = if (character.species.isBlank()) "Not specified" else character.species,
            valueColor = if (character.species.isBlank()) 
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}
