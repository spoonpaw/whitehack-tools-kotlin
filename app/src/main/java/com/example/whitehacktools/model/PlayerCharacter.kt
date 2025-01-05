package com.example.whitehacktools.model

import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

@Serializable
data class AttributeArray(
    val name: String,
    val attributes: Map<String, Int>
)

@Serializable
data class AttributeGroupPair(
    val attributeName: String,
    val groupType: GroupType,
    val groupName: String
)

@Serializable
enum class GroupType {
    Vocation,
    Species,
    Affiliation
}

@Serializable
data class PlayerCharacter(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val playerName: String,
    val characterClass: String,
    val level: Int,
    // Groups
    val vocation: String = "",
    val species: String = "",
    val affiliations: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    // Attributes
    val useDefaultAttributes: Boolean = true,
    val strength: Int = 10,
    val agility: Int = 10,
    val toughness: Int = 10,
    val intelligence: Int = 10,
    val willpower: Int = 10,
    val charisma: Int = 10,
    val customAttributeArray: AttributeArray? = null,
    val attributeGroupPairs: List<AttributeGroupPair> = emptyList(),
    // Combat Stats
    val currentHP: Int = 10,
    val maxHP: Int = 10,
    val attackValue: Int = 10,
    val defenseValue: Int = 10,
    val movement: Int = 30,
    val initiativeBonus: Int = 0,
    val savingValue: Int = 7,
    val saveColor: String = "",
    // Equipment
    val goldOnHand: Int = 0,
    val stashedGold: Int = 0,
    // Additional Info
    val experience: Int = 0,
    val corruption: Int = 0,
    val notes: String = "",
    val attunementSlots: List<AttunementSlot> = listOf(
        AttunementSlot(),
        AttunementSlot(),
        AttunementSlot(),
        AttunementSlot()
    ),
    // Strong Features
    @Serializable
    val strongCombatOptions: StrongCombatOptions? = null,
    @Serializable
    val conflictLoot: ConflictLoot? = null,
    // Deft Features
    val attunements: List<Attunement> = emptyList(),
    // Wise Features
    val miracles: List<String> = emptyList(),
    val wiseMiracles: WiseMiracles = WiseMiracles(),
    // Brave Features
    val braveAbilities: BraveAbilities = BraveAbilities(),
    val cleverAbilities: CleverAbilities = CleverAbilities()
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
        return when {
            useDefaultAttributes -> when (name.lowercase()) {
                "strength" -> strength
                "agility" -> agility
                "toughness" -> toughness
                "intelligence" -> intelligence
                "willpower" -> willpower
                "charisma" -> charisma
                else -> 10
            }
            else -> customAttributeArray?.attributes?.get(name) ?: 10
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
