package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.AttributeArray
import com.example.whitehacktools.model.PlayerCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributesFormCard(
    useDefaultAttributes: Boolean,
    onUseDefaultAttributesChange: (Boolean) -> Unit,
    strength: String,
    onStrengthChange: (String) -> Unit,
    agility: String,
    onAgilityChange: (String) -> Unit,
    toughness: String,
    onToughnessChange: (String) -> Unit,
    intelligence: String,
    onIntelligenceChange: (String) -> Unit,
    willpower: String,
    onWillpowerChange: (String) -> Unit,
    charisma: String,
    onCharismaChange: (String) -> Unit,
    customAttributeArray: AttributeArray?,
    onCustomAttributeArrayChange: (AttributeArray?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddAttributeDialog by remember { mutableStateOf(false) }
    var newAttributeName by remember { mutableStateOf("") }
    var newAttributeValue by remember { mutableStateOf("10") }

    SectionCard(
        title = "Attributes",
        modifier = modifier
    ) {
        // Segmented button group for attribute type selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = { onUseDefaultAttributesChange(true) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (useDefaultAttributes) 
                        MaterialTheme.colorScheme.secondaryContainer 
                    else 
                        MaterialTheme.colorScheme.surface,
                    contentColor = if (useDefaultAttributes)
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Default Attributes")
            }
            
            FilledTonalButton(
                onClick = { onUseDefaultAttributesChange(false) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (!useDefaultAttributes) 
                        MaterialTheme.colorScheme.secondaryContainer 
                    else 
                        MaterialTheme.colorScheme.surface,
                    contentColor = if (!useDefaultAttributes)
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Custom Attributes")
            }
        }

        if (useDefaultAttributes) {
            // Default attributes form
            FormField(
                value = strength,
                onValueChange = onStrengthChange,
                label = "Strength",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = agility,
                onValueChange = onAgilityChange,
                label = "Agility",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = toughness,
                onValueChange = onToughnessChange,
                label = "Toughness",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = intelligence,
                onValueChange = onIntelligenceChange,
                label = "Intelligence",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = willpower,
                onValueChange = onWillpowerChange,
                label = "Willpower",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = charisma,
                onValueChange = onCharismaChange,
                label = "Charisma",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
        } else {
            // Custom attributes form
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                customAttributeArray?.attributes?.forEach { (name, value) ->
                    FormField(
                        value = value.toString(),
                        onValueChange = { newValue ->
                            val updatedAttributes = customAttributeArray.attributes.toMutableMap()
                            updatedAttributes[name] = newValue.toIntOrNull() ?: 10
                            onCustomAttributeArrayChange(
                                customAttributeArray.copy(attributes = updatedAttributes)
                            )
                        },
                        label = name,
                        keyboardType = KeyboardType.Number,
                        numberOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val updatedAttributes = customAttributeArray.attributes.toMutableMap()
                                    updatedAttributes.remove(name)
                                    onCustomAttributeArrayChange(
                                        if (updatedAttributes.isEmpty()) null
                                        else customAttributeArray.copy(attributes = updatedAttributes)
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete attribute",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                Button(
                    onClick = { showAddAttributeDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add attribute")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Attribute")
                }
            }
        }
    }

    if (showAddAttributeDialog) {
        AlertDialog(
            onDismissRequest = { showAddAttributeDialog = false },
            title = { Text("Add Attribute") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField(
                        value = newAttributeName,
                        onValueChange = { newAttributeName = it },
                        label = "Attribute Name"
                    )
                    FormField(
                        value = newAttributeValue,
                        onValueChange = { newAttributeValue = it },
                        label = "Value",
                        keyboardType = KeyboardType.Number,
                        numberOnly = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newAttributeName.isNotBlank()) {
                            val updatedAttributes = customAttributeArray?.attributes?.toMutableMap() ?: mutableMapOf()
                            updatedAttributes[newAttributeName] = newAttributeValue.toIntOrNull() ?: 10
                            onCustomAttributeArrayChange(
                                customAttributeArray?.copy(attributes = updatedAttributes)
                                    ?: AttributeArray("Custom", updatedAttributes)
                            )
                            showAddAttributeDialog = false
                            newAttributeName = ""
                            newAttributeValue = "10"
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAttributeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
