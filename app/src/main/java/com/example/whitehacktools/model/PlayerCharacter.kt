package com.example.whitehacktools.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AttributeArray(
    val name: String,
    val attributes: Map<String, Int>
)

@Serializable
data class PlayerCharacter(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val playerName: String,
    val characterClass: String,
    val level: Int,
    val experience: Int = 0,
    // Groups
    val vocation: String = "",
    val species: String = "",
    // Attributes
    val useDefaultAttributes: Boolean = true,
    val strength: Int = 10,
    val agility: Int = 10,
    val toughness: Int = 10,
    val intelligence: Int = 10,
    val willpower: Int = 10,
    val charisma: Int = 10,
    val customAttributeArray: AttributeArray? = null,
    // Combat Stats
    val currentHP: Int = 10,
    val maxHP: Int = 10,
    val attackValue: Int = 10,
    val defenseValue: Int = 10,
    val movement: Int = 30,
    val initiativeBonus: Int = 0,
    val saveColor: String = "",
    // Equipment
    val goldOnHand: Int = 0,
    val stashedGold: Int = 0
) {
    companion object {
        val DEFAULT_ATTRIBUTES = listOf(
            "Strength",
            "Agility",
            "Toughness",
            "Intelligence",
            "Willpower",
            "Charisma"
        )
    }

    fun getAttributeValue(name: String): Int {
        return if (useDefaultAttributes) {
            when (name.lowercase()) {
                "strength" -> strength
                "agility" -> agility
                "toughness" -> toughness
                "intelligence" -> intelligence
                "willpower" -> willpower
                "charisma" -> charisma
                else -> 10
            }
        } else {
            customAttributeArray?.attributes?.get(name) ?: 10
        }
    }

    fun getAttributeNames(): List<String> {
        return if (useDefaultAttributes) {
            DEFAULT_ATTRIBUTES
        } else {
            customAttributeArray?.attributes?.keys?.toList() ?: DEFAULT_ATTRIBUTES
        }
    }
}
