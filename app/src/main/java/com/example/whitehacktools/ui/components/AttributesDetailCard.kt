package com.example.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.GroupType
import com.example.whitehacktools.model.PlayerCharacter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AttributesDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Attributes",
        modifier = modifier
    ) {
        val attributes = if (character.useDefaultAttributes) {
            mapOf(
                "Strength" to character.strength,
                "Agility" to character.agility,
                "Toughness" to character.toughness,
                "Intelligence" to character.intelligence,
                "Willpower" to character.willpower,
                "Charisma" to character.charisma
            )
        } else {
            character.customAttributeArray?.attributes ?: mapOf()
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (attributes.isEmpty()) {
                Text(
                    text = "No custom attributes added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                val attributesList = attributes.toList()
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    maxItemsInEachRow = 2
                ) {
                    attributesList.forEach { (name, value) ->
                        AttributeCard(
                            name = name,
                            value = value.toString(),
                            groups = character.attributeGroupPairs.filter { it.effectiveAttributeName == name },
                            character = character,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AttributeCard(
    name: String,
    value: String,
    groups: List<com.example.whitehacktools.model.AttributeGroupPair>,
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (groups.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    groups.forEach { pair ->
                        val groupType = pair.groupType ?: GroupType.Vocation // Default to Vocation if null
                        val groupName = pair.groupName ?: pair.group ?: "" // Use Swift format if Kotlin format is null
                        if (groupName.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = getGroupColor(groupType, character).copy(alpha = 0.1f),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = getGroupColor(groupType, character).copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = groupName,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = getGroupColor(groupType, character),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getGroupColor(type: GroupType, character: PlayerCharacter): Color {
    return when (type) {
        GroupType.Vocation -> MaterialTheme.colorScheme.primary
        GroupType.Species -> MaterialTheme.colorScheme.secondary
        GroupType.Affiliation -> MaterialTheme.colorScheme.tertiary
    }
}
