package com.example.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.GroupType
import com.example.whitehacktools.model.PlayerCharacter

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val attributesList = attributes.toList()
            for (i in attributesList.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (i + 1 >= attributesList.size) Arrangement.Center else Arrangement.SpaceBetween
                ) {
                    AttributeCard(
                        name = attributesList[i].first,
                        value = attributesList[i].second.toString(),
                        groups = character.attributeGroupPairs.filter { it.attributeName == attributesList[i].first },
                        character = character,
                        modifier = Modifier
                            .width(140.dp)
                            .heightIn(min = 120.dp)
                    )
                    if (i + 1 < attributesList.size) {
                        Spacer(modifier = Modifier.width(16.dp))
                        AttributeCard(
                            name = attributesList[i + 1].first,
                            value = attributesList[i + 1].second.toString(),
                            groups = character.attributeGroupPairs.filter { it.attributeName == attributesList[i + 1].first },
                            character = character,
                            modifier = Modifier
                                .width(140.dp)
                                .heightIn(min = 120.dp)
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
                        Surface(
                            color = getGroupColor(pair.groupType, character).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = pair.groupName,
                                style = MaterialTheme.typography.bodySmall,
                                color = getGroupColor(pair.groupType, character),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
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
        GroupType.Species -> MaterialTheme.colorScheme.primary
        GroupType.Vocation -> MaterialTheme.colorScheme.tertiary
        GroupType.Affiliation -> MaterialTheme.colorScheme.secondary
    }
}
