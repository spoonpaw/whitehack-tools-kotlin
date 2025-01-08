package com.example.whitehacktools.model

import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import com.example.whitehacktools.model.Weapon
import com.example.whitehacktools.model.Armor
import com.example.whitehacktools.model.Gear
import com.example.whitehacktools.model.AttunementSlot
import com.example.whitehacktools.model.StrongCombatOptions
import com.example.whitehacktools.model.ConflictLoot
import com.example.whitehacktools.model.Attunement
import com.example.whitehacktools.model.BraveAbilities
import com.example.whitehacktools.model.CleverAbilities
import com.example.whitehacktools.model.FortunateOptions
import com.example.whitehacktools.model.WiseMiracle
import com.example.whitehacktools.model.WiseMiracles
import com.example.whitehacktools.model.WiseMiracleSlot
import com.example.whitehacktools.model.BraveQuirkOptions

@Serializable
enum class GroupType {
    Vocation,
    Species,
    Affiliation
}

@Serializable
data class AttributeArray(
    val name: String,
    val attributes: Map<String, Int>
)

@Serializable
data class AttributeGroupPair(
    val attribute: String? = null,  // Swift format
    val group: String? = null,      // Swift format
    val attributeName: String? = null, // Kotlin format
    val groupType: GroupType? = null,  // Kotlin format
    val groupName: String? = null,     // Kotlin format
    val id: String = UUID.randomUUID().toString()
) {
    val effectiveAttributeName: String
        get() = attribute ?: attributeName ?: ""
        
    val effectiveGroupName: String
        get() = group ?: groupName ?: ""
}

@Serializable
data class Miracle(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isActive: Boolean = false,
    val isAdditional: Boolean = false
)

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
    @SerialName("vocationGroup")  // Match Swift field name
    val vocationGroup: String? = null,  // Swift format
    @SerialName("speciesGroup")   // Match Swift field name
    val speciesGroup: String? = null,   // Swift format
    @SerialName("affiliationGroups")  // Match Swift field name
    val affiliationGroups: List<String>? = null, // Swift format
    val affiliations: List<String> = emptyList(), // Kotlin format
    val languages: List<String> = emptyList(),
    // Attributes
    val useDefaultAttributes: Boolean = true,
    val useCustomAttributes: Boolean? = null, // Swift format
    val strength: Int = 10,
    val agility: Int = 10,
    val toughness: Int = 10,
    val intelligence: Int = 10,
    val willpower: Int = 10,
    val charisma: Int = 10,
    @Contextual
    val customAttributeArray: AttributeArray? = null,
    val attributeGroupPairs: List<AttributeGroupPair> = emptyList(),
    // Combat Stats
    val currentHP: Int = 10,
    val maxHP: Int = 10,
    val attackValue: Int = 10,
    val _attackValue: Int? = null,  // Swift format
    val defenseValue: Int = 10,
    val movement: Int = 30,
    val initiativeBonus: Int = 0,
    val savingValue: Int = 7,
    val saveColor: String = "",
    // Equipment
    val coinsOnHand: Int = 0,
    val stashedCoins: Int = 0,
    @Contextual
    val weapons: List<Weapon> = emptyList(),
    @Contextual
    val armor: List<Armor> = emptyList(),
    @Contextual
    val gear: List<Gear> = emptyList(),
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
    @SerialName("strongCombatOptions")
    val strongCombatOptions: StrongCombatOptions = StrongCombatOptions(),
    @SerialName("currentConflictLoot")
    val conflictLoot: ConflictLoot? = null,
    // Deft Features
    val attunements: List<Attunement> = emptyList(),
    // Wise Features
    val wiseMiracleSlots: List<WiseMiracleSlot> = emptyList(),
    // Brave Features
    @SerialName("braveQuirkOptions")
    val braveQuirkOptions: BraveQuirkOptions = BraveQuirkOptions(),
    @SerialName("comebackDice")
    val comebackDice: Int = 0,
    @SerialName("hasUsedSayNo")
    val hasUsedSayNo: Boolean = false,
    // Clever Features
    val cleverAbilities: CleverAbilities = CleverAbilities(),
    // Fortunate Features
    val fortunateOptions: FortunateOptions = FortunateOptions()
) {
    companion object {
        private const val TAG = "PlayerCharacter"
        
        val DEFAULT_ATTRIBUTES = listOf(
            "Strength",
            "Agility",
            "Toughness",
            "Intelligence",
            "Willpower",
            "Charisma"
        )

        fun clampAttribute(value: Int): Int = value.coerceIn(1..20)
    }

    val wiseMiracles: WiseMiracles
        get() = WiseMiracles(slots = wiseMiracleSlots)

    val effectiveVocation: String
        get() = vocationGroup ?: vocation

    val effectiveSpecies: String
        get() = speciesGroup ?: species

    val effectiveAffiliations: List<String>
        get() = affiliationGroups ?: affiliations

    fun calculateInitiativeBonus(): Int {
        if (!useDefaultAttributes) return 0
        return when {
            agility >= 16 -> 2
            agility >= 13 -> 1
            else -> 0
        }
    }

    // Calculate defense value from equipped armor
    fun calculateDefenseValue(): Int {
        return armor.filter { it.isEquipped }.sumOf { it.df + it.bonus }
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
            customAttributeArray != null -> customAttributeArray.attributes[name] ?: 10
            else -> 10
        }
    }

    fun getAttributeNames(): List<String> {
        return if (useDefaultAttributes) {
            DEFAULT_ATTRIBUTES
        } else {
            customAttributeArray?.attributes?.keys?.toList() ?: DEFAULT_ATTRIBUTES
        }
    }

    fun fixOutOfRangeAttributes(): PlayerCharacter {
        return if (useDefaultAttributes) {
            copy(
                strength = clampAttribute(strength),
                agility = clampAttribute(agility),
                toughness = clampAttribute(toughness),
                intelligence = clampAttribute(intelligence),
                willpower = clampAttribute(willpower),
                charisma = clampAttribute(charisma)
            )
        } else {
            customAttributeArray?.let { attrArray ->
                val fixedAttributes = attrArray.attributes.mapValues { (_, value) ->
                    clampAttribute(value)
                }
                copy(customAttributeArray = attrArray.copy(attributes = fixedAttributes))
            } ?: this
        }
    }

    fun copyWithNewId(): PlayerCharacter {
        return this.copy(
            id = UUID.randomUUID().toString(),
            weapons = weapons.map { it.copy(id = UUID.randomUUID().toString()) },
            armor = armor.map { it.copy(id = UUID.randomUUID().toString()) },
            gear = gear.map { it.copy(id = UUID.randomUUID().toString()) }
        )
    }

    fun getAllMiracles(): List<String> {
        android.util.Log.d(TAG, "Getting all miracles from ${wiseMiracleSlots.size} slots")
        wiseMiracleSlots.forEachIndexed { index, slot ->
            android.util.Log.d(TAG, "Slot $index:")
            android.util.Log.d(TAG, "  Base miracles: ${slot.baseMiracles.map { it.name }}")
            android.util.Log.d(TAG, "  Additional miracles: ${slot.additionalMiracles.map { it.name }}")
        }
        
        return wiseMiracleSlots.flatMap { slot ->
            slot.miracles.filter { miracle -> miracle.name.isNotEmpty() }
                .map { miracle -> miracle.name }
        }
    }
}
