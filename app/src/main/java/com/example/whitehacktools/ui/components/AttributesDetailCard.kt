package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AttributesDetailCard(
    strength: Int,
    agility: Int,
    toughness: Int,
    intelligence: Int,
    willpower: Int,
    charisma: Int,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Attributes",
        modifier = modifier
    ) {
        DetailItem(
            label = "Strength",
            value = strength.toString()
        )
        DetailItem(
            label = "Agility",
            value = agility.toString()
        )
        DetailItem(
            label = "Toughness",
            value = toughness.toString()
        )
        DetailItem(
            label = "Intelligence",
            value = intelligence.toString()
        )
        DetailItem(
            label = "Willpower",
            value = willpower.toString()
        )
        DetailItem(
            label = "Charisma",
            value = charisma.toString()
        )
    }
}
