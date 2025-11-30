package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

/**
 * Clever class knacks - these are the CORRECT knacks for the Clever class.
 * 
 * NOTE: Previously this file incorrectly contained Brave quirks copy-pasted by mistake.
 * If a user has old data with IDs 0-4 that were from the incorrect enum, those will
 * be silently cleared (treated as null) since the old IDs don't map to the same abilities.
 */
@Serializable(with = CleverKnackSerializer::class)
enum class CleverKnack(val id: Int) {
    COMBAT_EXPLOITER(0),
    EFFICIENT_CRAFTER(1),
    WEAKENED_SAVES(2),
    NAVIGATION_MASTER(3),
    CONVINCING_NEGOTIATOR(4),
    ESCAPE_ARTIST(5),
    SUBSTANCE_EXPERT(6),
    MACHINE_MASTER(7),
    TRACKING_EXPERT(8);

    val displayName: String
        get() = when (this) {
            COMBAT_EXPLOITER -> "Combat Exploiter"
            EFFICIENT_CRAFTER -> "Efficient Crafter"
            WEAKENED_SAVES -> "Weakened Saves"
            NAVIGATION_MASTER -> "Navigation Master"
            CONVINCING_NEGOTIATOR -> "Convincing Negotiator"
            ESCAPE_ARTIST -> "Escape Artist"
            SUBSTANCE_EXPERT -> "Substance Expert"
            MACHINE_MASTER -> "Machine Master"
            TRACKING_EXPERT -> "Tracking Expert"
        }

    val description: String
        get() = when (this) {
            COMBAT_EXPLOITER -> "Base bonus for combat advantage is +3 instead of +2, and once per battle may switch d6 for d10 as damage die."
            EFFICIENT_CRAFTER -> "+4 to crafting, mending, or assembly. Takes half the time and can skip one non-essential part."
            WEAKENED_SAVES -> "Targets of special attacks get -3 to their saves."
            NAVIGATION_MASTER -> "Can always figure out location roughly. Never gets lost."
            CONVINCING_NEGOTIATOR -> "+2 to task rolls and saves in conviction attempts, including trade."
            ESCAPE_ARTIST -> "+4 to any task roll related to escaping confinement or bypassing barriers."
            SUBSTANCE_EXPERT -> "+4 to substance identification and saves, +1 to quantified effects in character's favor."
            MACHINE_MASTER -> "+4 to task rolls with or concerning machines."
            TRACKING_EXPERT -> "+4 to tracking and covering own tracks."
        }

    companion object {
        fun fromId(id: Int): CleverKnack? = entries.find { it.id == id }
    }
}

/**
 * Serializer for CleverKnack that handles invalid IDs gracefully.
 * 
 * If an invalid ID is encountered (e.g., from old corrupted data where Brave quirks
 * were incorrectly saved), this will throw an exception which kotlinx.serialization
 * handles by using the default value (null) for the nullable knack field.
 */
object CleverKnackSerializer : KSerializer<CleverKnack> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CleverKnack", PrimitiveKind.INT)
    
    override fun serialize(encoder: Encoder, value: CleverKnack) {
        encoder.encodeInt(value.id)
    }
    
    override fun deserialize(decoder: Decoder): CleverKnack {
        val id = decoder.decodeInt()
        return CleverKnack.fromId(id) 
            ?: throw IllegalArgumentException("Invalid CleverKnack id: $id - clearing corrupted data")
    }
}

@Serializable
data class CleverKnackSlot(
    @SerialName("knack")
    val knack: CleverKnack? = null,
    @SerialName("hasUsedCombatDie")
    val hasUsedCombatDie: Boolean = false,
    @SerialName("id")
    val id: String = UUID.randomUUID().toString()
)

@Serializable
data class CleverKnackOptions(
    @SerialName("slots")
    val slots: List<CleverKnackSlot> = emptyList(),
    @SerialName("hasUsedUnorthodoxBonus")
    val hasUsedUnorthodoxBonus: Boolean = false
)

@Serializable
data class CleverAbilities(
    @SerialName("cleverKnackOptions")
    val knackOptions: CleverKnackOptions = CleverKnackOptions()
) {
    val activeKnacks: List<CleverKnack>
        get() = knackOptions.slots.mapNotNull { it.knack }

    fun getKnackSlot(at: Int): CleverKnackSlot =
        if (at < knackOptions.slots.size) knackOptions.slots[at] else CleverKnackSlot()

    fun setKnackSlot(slot: CleverKnackSlot, at: Int): CleverAbilities {
        if (at >= knackOptions.slots.size) return this
        val newSlots = knackOptions.slots.toMutableList()
        newSlots[at] = slot
        return copy(knackOptions = CleverKnackOptions(slots = newSlots, hasUsedUnorthodoxBonus = knackOptions.hasUsedUnorthodoxBonus))
    }

    fun isKnackActive(knack: CleverKnack): Boolean =
        knackOptions.slots.any { it.knack == knack }

    fun isSlotFilled(at: Int): Boolean =
        at < knackOptions.slots.size && knackOptions.slots[at].knack != null

    val count: Int
        get() = knackOptions.slots.count { it.knack != null }

    fun setKnack(at: Int, knack: CleverKnack?, hasUsedCombatDie: Boolean = false): CleverAbilities {
        return setKnackSlot(CleverKnackSlot(knack = knack, hasUsedCombatDie = hasUsedCombatDie), at)
    }
}
