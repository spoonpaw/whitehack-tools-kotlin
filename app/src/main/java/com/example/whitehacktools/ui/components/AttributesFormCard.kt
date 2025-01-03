package com.example.whitehacktools.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AttributesFormCard(
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
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Attributes",
        modifier = modifier
    ) {
        FormField(
            value = strength,
            onValueChange = onStrengthChange,
            label = "Strength",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = strength.toIntOrNull() !in 1..20
        )
        
        FormField(
            value = agility,
            onValueChange = onAgilityChange,
            label = "Agility",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = agility.toIntOrNull() !in 1..20
        )
        
        FormField(
            value = toughness,
            onValueChange = onToughnessChange,
            label = "Toughness",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = toughness.toIntOrNull() !in 1..20
        )
        
        FormField(
            value = intelligence,
            onValueChange = onIntelligenceChange,
            label = "Intelligence",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = intelligence.toIntOrNull() !in 1..20
        )
        
        FormField(
            value = willpower,
            onValueChange = onWillpowerChange,
            label = "Willpower",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = willpower.toIntOrNull() !in 1..20
        )
        
        FormField(
            value = charisma,
            onValueChange = onCharismaChange,
            label = "Charisma",
            keyboardType = KeyboardType.Number,
            numberOnly = true,
            validate = { value ->
                value.isEmpty() || (value.toIntOrNull() ?: 0) in 1..20
            },
            isError = charisma.toIntOrNull() !in 1..20
        )
    }
}
