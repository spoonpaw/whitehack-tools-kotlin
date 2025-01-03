package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter

@Composable
fun AttributesDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Attributes",
        modifier = modifier
    ) {
        if (character.useDefaultAttributes) {
            // Display default attributes
            DetailItem(
                label = "Strength",
                value = character.strength.toString()
            )
            DetailItem(
                label = "Agility",
                value = character.agility.toString()
            )
            DetailItem(
                label = "Toughness",
                value = character.toughness.toString()
            )
            DetailItem(
                label = "Intelligence",
                value = character.intelligence.toString()
            )
            DetailItem(
                label = "Willpower",
                value = character.willpower.toString()
            )
            DetailItem(
                label = "Charisma",
                value = character.charisma.toString()
            )
        } else {
            // Display custom attributes
            character.customAttributeArray?.let { attributeArray ->
                Text(
                    text = attributeArray.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    attributeArray.attributes.forEach { (name, value) ->
                        DetailItem(
                            label = name,
                            value = value.toString()
                        )
                    }
                }
            }
        }
    }
}
