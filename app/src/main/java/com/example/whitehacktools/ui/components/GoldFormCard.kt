package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoldFormCard(
    goldOnHand: String,
    onGoldOnHandChange: (String) -> Unit,
    stashedGold: String,
    onStashedGoldChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalGold = (goldOnHand.toIntOrNull() ?: 0) + (stashedGold.toIntOrNull() ?: 0)

    SectionCard(
        title = "Gold",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gold on Hand
            FormField(
                value = goldOnHand,
                onValueChange = onGoldOnHandChange,
                label = "Gold on Hand",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            
            // Stashed Gold
            FormField(
                value = stashedGold,
                onValueChange = onStashedGoldChange,
                label = "Stashed Gold",
                keyboardType = KeyboardType.Number,
                numberOnly = true
            )
            
            // Total Gold
            DetailItem(
                label = "Total Gold",
                value = totalGold.toString()
            )
        }
    }
}
