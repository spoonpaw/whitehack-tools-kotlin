package com.netartisancollective.whitehacktools.ui.components

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
    val maxHPValue = maxHP.toIntOrNull() ?: 0
    val currentHPValue = currentHP.toIntOrNull() ?: 0
    val isCurrentHPValid = currentHP.isEmpty() || currentHP == "-" || 
        (currentHPValue <= maxHPValue && currentHP.toIntOrNull() != null)

    SectionCard(
        title = "Combat Stats",
        modifier = modifier
    ) {
        FormField(
            value = currentHP,
            onValueChange = onCurrentHPChange,
            label = "Current HP",
            keyboardType = KeyboardType.Number,
            numberOnly = false, // Allow minus sign
            validate = { value ->
                // Allow empty, minus sign, or valid integer that doesn't exceed max
                val intValue = value.toIntOrNull()
                value.isEmpty() || value == "-" || 
                    (intValue != null && intValue <= maxHPValue)
            },
            isError = !isCurrentHPValid
        )
        
        FormField(
            value = maxHP,
            onValueChange = onMaxHPChange,
            label = "Max HP",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                // Allow empty or positive integer >= 1
                value.isEmpty() || (value.toIntOrNull() ?: 0) >= 1
            },
            isError = maxHP.isNotEmpty() && (maxHP.toIntOrNull() ?: 0) < 1
        )
        
        FormField(
            value = movement,
            onValueChange = onMovementChange,
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
