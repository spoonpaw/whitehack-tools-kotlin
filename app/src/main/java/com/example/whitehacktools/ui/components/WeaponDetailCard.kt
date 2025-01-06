package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeaponDetailCard(
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Weapons",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Placeholder for weapon details")
            Text(text = "• Weapon name: Longsword")
            Text(text = "• Damage: 1d8")
            Text(text = "• Properties: Versatile")
        }
    }
}
