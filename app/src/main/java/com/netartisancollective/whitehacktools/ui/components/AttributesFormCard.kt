package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.AttributeArray
import com.netartisancollective.whitehacktools.model.AttributeGroupPair

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
    attributeGroupPairs: List<AttributeGroupPair>,
    onAttributeGroupPairsChange: (List<AttributeGroupPair>) -> Unit,
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
                onClick = { 
                    // Remove any attribute-group pairs for custom attributes
                    if (!useDefaultAttributes) {
                        val defaultAttributeNames = setOf("Strength", "Agility", "Toughness", "Intelligence", "Willpower", "Charisma")
                        onAttributeGroupPairsChange(attributeGroupPairs.filter { it.attributeName in defaultAttributeNames })
                    }
                    onUseDefaultAttributesChange(true)
                },
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
                onClick = { 
                    // Remove any attribute-group pairs for default attributes
                    if (useDefaultAttributes) {
                        val defaultAttributeNames = setOf("Strength", "Agility", "Toughness", "Intelligence", "Willpower", "Charisma")
                        onAttributeGroupPairsChange(attributeGroupPairs.filter { it.attributeName !in defaultAttributeNames })
                    }
                    onUseDefaultAttributesChange(false)
                },
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
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onStrengthChange(newValue)
                    }
                },
                label = "Strength",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = agility,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onAgilityChange(newValue)
                    }
                },
                label = "Agility",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = toughness,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onToughnessChange(newValue)
                    }
                },
                label = "Toughness",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = intelligence,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onIntelligenceChange(newValue)
                    }
                },
                label = "Intelligence",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = willpower,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onWillpowerChange(newValue)
                    }
                },
                label = "Willpower",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            FormField(
                value = charisma,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                        onCharismaChange(newValue)
                    }
                },
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
                if (customAttributeArray?.attributes?.isEmpty() != false) {
                    Text(
                        text = "No custom attributes added",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                customAttributeArray?.attributes?.forEach { (name, value) ->
                    var isFieldFocused by remember { mutableStateOf(false) }
                    var textValue by remember(value) { mutableStateOf(value.toString()) }
                    
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { newValue ->
                            // Allow empty values and valid numbers between 1-20
                            val intValue = newValue.toIntOrNull()
                            if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                                textValue = newValue
                                val updatedAttributes = customAttributeArray.attributes.toMutableMap()
                                updatedAttributes[name] = intValue ?: value
                                onCustomAttributeArrayChange(
                                    customAttributeArray.copy(attributes = updatedAttributes)
                                )
                            }
                        },
                        label = { Text(name) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused && isFieldFocused) {
                                    // When losing focus, if empty or invalid, set to default
                                    if (textValue.isEmpty() || textValue.toIntOrNull() !in 1..20) {
                                        textValue = "10"
                                        val updatedAttributes = customAttributeArray.attributes.toMutableMap()
                                        updatedAttributes[name] = 10
                                        onCustomAttributeArrayChange(
                                            customAttributeArray.copy(attributes = updatedAttributes)
                                        )
                                    }
                                }
                                isFieldFocused = focusState.isFocused
                            },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val updatedAttributes = customAttributeArray.attributes.toMutableMap()
                                    updatedAttributes.remove(name)
                                    // Remove any attribute-group pairs that use this attribute
                                    val updatedPairs = attributeGroupPairs.filter { it.attributeName != name }
                                    onAttributeGroupPairsChange(updatedPairs)
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
        var isValueFieldFocused by remember { mutableStateOf(false) }
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
                    OutlinedTextField(
                        value = newAttributeValue,
                        onValueChange = { newValue ->
                            // Allow empty values and valid numbers between 1-20
                            val intValue = newValue.toIntOrNull()
                            if (newValue.isEmpty() || (intValue != null && intValue in 1..20)) {
                                newAttributeValue = newValue
                            }
                        },
                        label = { Text("Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused && isValueFieldFocused) {
                                    // When losing focus, if empty or invalid, set to default
                                    val currentValue = newAttributeValue.toIntOrNull()
                                    if (currentValue == null || currentValue !in 1..20) {
                                        newAttributeValue = "10"
                                    }
                                }
                                isValueFieldFocused = focusState.isFocused
                            },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
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
