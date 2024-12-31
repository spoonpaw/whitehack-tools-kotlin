package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombatFormCard(
    currentHP: String,
    onCurrentHPChange: (String) -> Unit,
    maxHP: String,
    onMaxHPChange: (String) -> Unit,
    attackValue: String,
    onAttackValueChange: (String) -> Unit,
    defenseValue: String,
    onDefenseValueChange: (String) -> Unit,
    movement: String,
    onMovementChange: (String) -> Unit,
    initiativeBonus: String,
    onInitiativeBonusChange: (String) -> Unit,
    saveColor: String,
    onSaveColorChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Combat Stats",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current HP
            OutlinedTextField(
                value = currentHP,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() || it == '-' }
                    onCurrentHPChange(newValue)
                },
                label = { 
                    Text(
                        text = "Current HP",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Max HP
            OutlinedTextField(
                value = maxHP,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() }
                    onMaxHPChange(newValue)
                },
                label = { 
                    Text(
                        text = "Max HP",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Attack Value
            OutlinedTextField(
                value = attackValue,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() }
                    onAttackValueChange(newValue)
                },
                label = { 
                    Text(
                        text = "Attack Value",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Defense Value
            OutlinedTextField(
                value = defenseValue,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() }
                    onDefenseValueChange(newValue)
                },
                label = { 
                    Text(
                        text = "Defense Value",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Movement
            OutlinedTextField(
                value = movement,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() }
                    onMovementChange(newValue)
                },
                label = { 
                    Text(
                        text = "Movement",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Initiative Bonus
            OutlinedTextField(
                value = initiativeBonus,
                onValueChange = { value ->
                    val newValue = value.filter { it.isDigit() || it == '+' || it == '-' }
                    onInitiativeBonusChange(newValue)
                },
                label = { 
                    Text(
                        text = "Initiative Bonus",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Save Color
            OutlinedTextField(
                value = saveColor,
                onValueChange = onSaveColorChange,
                label = { 
                    Text(
                        text = "Save Color",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
