package com.example.whitehacktools.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlayerCharacter(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val playerName: String,
    val characterClass: String,
    val level: Int,
    val experience: Int = 0,
    // Attributes
    val strength: Int = 10,
    val agility: Int = 10,
    val toughness: Int = 10,
    val intelligence: Int = 10,
    val willpower: Int = 10,
    val charisma: Int = 10,
    // Combat Stats
    val currentHP: Int = 10,
    val maxHP: Int = 10,
    val attackValue: Int = 0,
    val defenseValue: Int = 0,
    val movement: Int = 30,
    val initiativeBonus: Int = 0,
    val saveColor: String = "",
    // Equipment
    val goldOnHand: Int = 0,
    val stashedGold: Int = 0
)
