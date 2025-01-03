package com.example.whitehacktools.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerCharacter(
    val id: String,
    val name: String,
    val playerName: String = "",
    val characterClass: String,
    val level: Int,
    val experience: Int = 0,
    // Combat Stats
    val currentHP: Int = 10,
    val maxHP: Int = 10,
    val attackValue: Int = 0,
    val defenseValue: Int = 0,
    val movement: Int = 30,
    val initiativeBonus: Int = 0,
    val saveColor: String = "",
    val goldOnHand: Int = 0,
    val stashedGold: Int = 0
)
