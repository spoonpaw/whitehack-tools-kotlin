package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArmorDetailCard(
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Armor",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Placeholder for armor details")
            Text(text = "• Armor type: Chain Mail")
            Text(text = "• AC: 16")
            Text(text = "• Properties: Heavy")
        }
    }
}
