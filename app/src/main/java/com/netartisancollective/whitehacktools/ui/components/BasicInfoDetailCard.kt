package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.PlayerCharacter
import com.netartisancollective.whitehacktools.utilities.AdvancementTables
import kotlin.math.max
import kotlin.math.min

@Composable
fun DynamicProgressBar(
    value: Float,
    maxValue: Float,
    label: String,
    foregroundColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    showPercentage: Boolean = true,
    isComplete: Boolean = false,
    completionMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val progress = if (maxValue > 0) min(max(value / maxValue, 0f), 1f) else 0f
    val percentage = if (maxValue > 0) min((value / maxValue * 100).toInt(), 100) else 0
    val barColor = if (isComplete) Color(0xFFFFD700) else foregroundColor

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (showPercentage) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = barColor,
            trackColor = backgroundColor
        )

        if (isComplete && !completionMessage.isNullOrEmpty()) {
            Text(
                text = completionMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BasicInfoDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Basic Info",
        modifier = modifier
    ) {
        DetailItem(
            label = "Name",
            value = character.name
        )
        
        DetailItem(
            label = "Level",
            value = character.level.toString()
        )
        
        DetailItem(
            label = "Character Class",
            value = character.characterClass
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Health Bar
        val healthColor = when {
            character.currentHP <= -10 -> Color(0x80FF0000) // Death state
            character.currentHP <= -2 -> Color(0xFFFFFF00) // Wounded
            character.currentHP == character.maxHP -> Color(0xFF00FF00) // Full health
            else -> {
                val healthPercentage = character.currentHP.toFloat() / character.maxHP.toFloat()
                Color(
                    android.graphics.Color.HSVToColor(
                        floatArrayOf(
                            120f * healthPercentage, // Hue: 0 to 120 (red to green)
                            0.8f, // Saturation
                            0.8f  // Value/Brightness
                        )
                    )
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DynamicProgressBar(
                value = character.currentHP.toFloat(),
                maxValue = character.maxHP.toFloat(),
                label = "Health (${character.currentHP}/${character.maxHP})",
                foregroundColor = healthColor,
                showPercentage = true
            )

            if (character.currentHP <= 0) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = when {
                                character.currentHP <= -10 -> "Instant death"
                                character.currentHP in -9..-2 -> "Knocked out until healed to positive hp, injured, save or die in d6 rounds"
                                character.currentHP == -1 -> "Knocked out until healed to positive hp, injured"
                                else -> "Knocked out until healed to positive hp"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // XP Bar (only show if level < 10)
        if (character.level < 10) {
            
            val currentLevelXp = if (character.level > 1) {
                AdvancementTables.xpRequirement(character.characterClass, character.level)
            } else 0
            
            val nextLevelXp = AdvancementTables.xpRequirement(character.characterClass, character.level + 1)
            
            val xpForCurrentLevel = character.experience - currentLevelXp
            val xpNeededForNextLevel = nextLevelXp - currentLevelXp
            val readyToLevelUp = xpForCurrentLevel >= xpNeededForNextLevel

            DynamicProgressBar(
                value = xpForCurrentLevel.toFloat(),
                maxValue = xpNeededForNextLevel.toFloat(),
                label = "XP to Level ${character.level + 1} (${xpForCurrentLevel}/${xpNeededForNextLevel})",
                foregroundColor = if (readyToLevelUp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                showPercentage = true
            )

            if (readyToLevelUp) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ready to level up!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
