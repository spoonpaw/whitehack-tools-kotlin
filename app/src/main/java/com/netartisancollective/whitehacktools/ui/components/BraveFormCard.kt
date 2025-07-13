package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.BraveQuirk
import com.netartisancollective.whitehacktools.model.BraveQuirkOptions
import com.netartisancollective.whitehacktools.model.BraveQuirkSlot
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BraveFormCard(
    characterClass: String,
    level: Int,
    braveQuirkOptions: BraveQuirkOptions,
    comebackDice: Int,
    hasUsedSayNo: Boolean,
    onBraveQuirkOptionsChanged: (BraveQuirkOptions) -> Unit,
    onComebackDiceChanged: (Int) -> Unit,
    onHasUsedSayNoChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Brave") {
        val availableSlots = remember(level) {
            AdvancementTables.stats("Brave", level).slots
        }

        // Initialize or resize quirks list if needed
        LaunchedEffect(availableSlots) {
            if (braveQuirkOptions.slots.size < 4) {
                // Create a new list with 4 slots, copying over existing values
                val newSlots = List(4) { index ->
                    if (index < braveQuirkOptions.slots.size) {
                        braveQuirkOptions.slots[index]
                    } else {
                        BraveQuirkSlot(null, "")
                    }
                }
                onBraveQuirkOptionsChanged(BraveQuirkOptions(slots = newSlots))
            }
        }

        SectionCard(
            title = "The Brave",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Comeback Dice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Comeback Dice:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = comebackDice.toString(),
                        onValueChange = { newValue ->
                            val newDice = newValue.toIntOrNull() ?: 0
                            onComebackDiceChanged(newDice)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(100.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    )
                }

                // Say No Power
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Used Say No Power:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = hasUsedSayNo,
                        onCheckedChange = onHasUsedSayNoChanged
                    )
                }

                // Quirks
                Text(
                    text = "Quirks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                braveQuirkOptions.slots.take(availableSlots).forEachIndexed { index, slot ->
                    var expanded by remember { mutableStateOf(false) }
                    var protectedAllyName by remember { mutableStateOf(slot.protectedAllyName) }

                    Column {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = slot.quirk?.displayName ?: "Select a Quirk",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                BraveQuirk.values().forEach { quirk ->
                                    if (!isQuirkActive(braveQuirkOptions, quirk) || quirk == slot.quirk) {
                                        DropdownMenuItem(
                                            text = { Text(quirk.displayName) },
                                            onClick = {
                                                val newSlots = braveQuirkOptions.slots.toMutableList()
                                                newSlots[index] = BraveQuirkSlot(quirk = quirk, protectedAllyName = protectedAllyName)
                                                onBraveQuirkOptionsChanged(BraveQuirkOptions(slots = newSlots))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                                // Add option to clear selection
                                DropdownMenuItem(
                                    text = { Text("Clear Selection") },
                                    onClick = {
                                        val newSlots = braveQuirkOptions.slots.toMutableList()
                                        newSlots[index] = BraveQuirkSlot(quirk = null, protectedAllyName = "")
                                        onBraveQuirkOptionsChanged(BraveQuirkOptions(slots = newSlots))
                                        expanded = false
                                    }
                                )
                            }
                        }

                        // Show Protected Ally Name field if PROTECT_ALLY is selected
                        if (slot.quirk == BraveQuirk.PROTECT_ALLY) {
                            OutlinedTextField(
                                value = protectedAllyName,
                                onValueChange = { newName ->
                                    protectedAllyName = newName
                                    val newSlots = braveQuirkOptions.slots.toMutableList()
                                    newSlots[index] = BraveQuirkSlot(quirk = BraveQuirk.PROTECT_ALLY, protectedAllyName = newName)
                                    onBraveQuirkOptionsChanged(BraveQuirkOptions(slots = newSlots))
                                },
                                label = { Text("Protected Ally Name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }

                        if (slot.quirk != null) {
                            Text(
                                text = slot.quirk.description,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun isQuirkActive(braveQuirkOptions: BraveQuirkOptions, quirk: BraveQuirk): Boolean =
    braveQuirkOptions.slots.any { it.quirk == quirk }
