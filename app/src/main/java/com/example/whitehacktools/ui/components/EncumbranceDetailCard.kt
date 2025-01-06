package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EncumbranceDetailCard(
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Encumbrance",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Current Load: Light")
            Text(text = "Maximum Carry Capacity: 10 items")
            Text(text = "Movement Penalty: None")
            Text(text = "Items Carried: 3/10")
        }
    }
}
