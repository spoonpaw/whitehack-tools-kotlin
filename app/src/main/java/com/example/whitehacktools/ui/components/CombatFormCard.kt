package com.example.whitehacktools.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CombatFormCard(
    currentHP: String,
    onCurrentHPChange: (String) -> Unit,
    maxHP: String,
    onMaxHPChange: (String) -> Unit,
    movement: String,
    onMovementChange: (String) -> Unit,
    saveColor: String,
    onSaveColorChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Combat Stats",
        modifier = modifier
    ) {
        FormField(
            value = currentHP,
            onValueChange = { value ->
                val newValue = value.filter { it.isDigit() || it == '-' }
                onCurrentHPChange(newValue)
            },
            label = "Current HP",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { it.isEmpty() || it.first() != '-' || it.count { c -> c == '-' } <= 1 }
        )
        
        FormField(
            value = maxHP,
            onValueChange = { value ->
                val newValue = value.filter { it.isDigit() }
                onMaxHPChange(newValue)
            },
            label = "Max HP",
            keyboardType = KeyboardType.Number,
            numberOnly = true
        )
        
        FormField(
            value = movement,
            onValueChange = { value ->
                val newValue = value.filter { it.isDigit() }
                onMovementChange(newValue)
            },
            label = "Movement",
            keyboardType = KeyboardType.Number,
            numberOnly = true
        )
        
        FormField(
            value = saveColor,
            onValueChange = onSaveColorChange,
            label = "Save Color"
        )
    }
}
