package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.whitehacktools.model.PlayerCharacter
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Shapes

enum class BurdenLevel {
    NORMAL, HEAVY, SEVERE, MASSIVE;

    @Composable
    fun color(): Color = when (this) {
        NORMAL -> MaterialTheme.colorScheme.primary
        HEAVY -> MaterialTheme.colorScheme.tertiary
        SEVERE -> MaterialTheme.colorScheme.error
        MASSIVE -> MaterialTheme.colorScheme.error
    }
}

@Composable
fun EncumbranceDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    val usedSlots = remember(character) { calculateUsedSlots(character) }
    val maxSlots = remember(character) { calculateMaxSlots(character) }
    val excessSlots = maxOf(0.0, usedSlots - maxSlots)
    val burdenLevel = remember(excessSlots) { calculateBurdenLevel(excessSlots) }
    val movementPenalty = (5 * ((excessSlots + 1) / 2)).toInt()
    val base = character.movement
    val movementRates = remember(base) {
        listOf(
            "${base} ft/r",
            "${base - 5} ft/r",
            "${base - 10} ft/r",
            "${base - 15} ft/r"
        )
    }
    val crawlRates = remember {
        listOf(
            "120 ft",
            "100 ft",
            "80 ft",
            "60 ft"
        )
    }

    SectionCard(
        title = "Encumbrance",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Slots Overview Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Slots Overview",
                    style = MaterialTheme.typography.titleMedium
                )

                // Used and Available Slots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Used Slots
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Used",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%.2f", usedSlots),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Available Slots
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%.2f", maxOf(0.0, maxSlots - usedSlots)),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Divider()

                // Max Slots
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Max Slots",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.0f", maxSlots),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (character.gear.any { it.isContainer && it.isEquipped }) 
                            "15 slots with equipped container" 
                        else 
                            "10 slots without container",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (excessSlots > 0) {
                    Divider()

                    // Excess Slots
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Excess",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = String.format("%.1f", excessSlots),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "slots over limit",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Current Burden Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Current Burden",
                    style = MaterialTheme.typography.titleMedium
                )

                // Movement Table
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Table Header
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Level",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Move",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Crawl",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Table Rows
                    BurdenLevel.values().forEachIndexed { index, level ->
                        val isCurrentBurden = level == burdenLevel
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = if (isCurrentBurden) level.color().copy(alpha = 0.15f) else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = level.name.lowercase().replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isCurrentBurden) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrentBurden) level.color() else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = movementRates[index],
                                    modifier = Modifier.width(80.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isCurrentBurden) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrentBurden) level.color() else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = crawlRates[index],
                                    modifier = Modifier.width(80.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isCurrentBurden) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrentBurden) level.color() else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            if (excessSlots > 0) {
                // Burden Status Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Burden Status",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Movement penalty: -$movementPenalty feet per round",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = when (burdenLevel) {
                            BurdenLevel.NORMAL -> "No burden effects"
                            BurdenLevel.HEAVY -> "Heavy burden: -1 to all physical actions"
                            BurdenLevel.SEVERE -> "Severe burden: -2 to all physical actions"
                            BurdenLevel.MASSIVE -> "Massive burden: -3 to all physical actions"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = burdenLevel.color()
                    )
                }
            }
        }
    }
}

private fun calculateBurdenLevel(excessSlots: Double): BurdenLevel {
    val drops = minOf(3, ((excessSlots + 1) / 2).toInt())
    return when (drops) {
        0 -> BurdenLevel.NORMAL
        1 -> BurdenLevel.HEAVY
        2 -> BurdenLevel.SEVERE
        else -> BurdenLevel.MASSIVE
    }
}

private fun calculateMaxSlots(character: PlayerCharacter): Double {
    val hasContainer = character.gear.any { it.isContainer && it.isEquipped }
    return if (hasContainer) 15.0 else 10.0
}

private fun calculateUsedSlots(character: PlayerCharacter): Double {
    var total = 0.0
    
    Log.d("Encumbrance", "Starting encumbrance calculation...")
    
    // Add non-stashed gear slots
    for (gear in character.gear.filter { !it.isStashed }) {
        val slotCount = when (gear.weight.lowercase()) {
            "no size" -> 0.01 // Will be counted in groups of 100
            "minor" -> 0.5
            "regular" -> 1.0
            "heavy" -> 2.0
            else -> 1.0
        }
        val slots = slotCount * gear.quantity
        Log.d("Encumbrance", "${gear.weight} item: ${gear.name} x${gear.quantity} = $slots slots")
        total += slots
    }
    
    // Add non-stashed armor slots
    for (armor in character.armor.filter { !it.isStashed }) {
        val slots = armor.weight.toDouble() * armor.quantity
        Log.d("Encumbrance", "Armor: ${armor.name} x${armor.quantity} = $slots slots")
        total += slots
    }
    
    // Add non-stashed weapon slots
    for (weapon in character.weapons.filter { !it.isStashed }) {
        val slotCount = when (weapon.weight.lowercase()) {
            "no size" -> 0.01
            "minor" -> 0.5
            "regular" -> 1.0
            "heavy" -> 2.0
            else -> 1.0
        }
        val slots = slotCount * weapon.quantity
        Log.d("Encumbrance", "${weapon.weight} weapon: ${weapon.name} x${weapon.quantity} = $slots slots")
        total += slots
    }
    
    // Add coins (100 coins = 1 slot)
    val coinSlots = character.goldOnHand.toDouble() / 100.0
    Log.d("Encumbrance", "Coins: ${character.goldOnHand} coins = $coinSlots slots")
    total += coinSlots
    
    Log.d("Encumbrance", "Total used slots: $total")
    return total
}
