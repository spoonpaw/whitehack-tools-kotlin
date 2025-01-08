package com.example.whitehacktools.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.whitehacktools.model.PlayerCharacter

@Composable
fun GoldDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Gold",
        modifier = modifier
    ) {
        DetailItem(
            label = "Gold on Hand",
            value = character.coinsOnHand.toString()
        )
        DetailItem(
            label = "Stashed Gold",
            value = character.stashedCoins.toString()
        )
        DetailItem(
            label = "Total Gold",
            value = (character.coinsOnHand + character.stashedCoins).toString()
        )
    }
}
