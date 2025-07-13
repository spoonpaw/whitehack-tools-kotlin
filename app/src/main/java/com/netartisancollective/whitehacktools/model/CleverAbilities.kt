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

@Serializable(with = CleverKnackSerializer::class)
enum class CleverKnack(val id: Int) {
    DOUBLE_STRAIN_ROLLS(0),
    IMPROVED_HEALING(1),
    PROTECT_ALLY(2),
    RESIST_CURSES(3),
    DRAW_ATTENTION(4);

    val displayName: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Double Strain Rolls"
            IMPROVED_HEALING -> "Improved Healing"
            PROTECT_ALLY -> "Protect Ally"
            RESIST_CURSES -> "Resist Curses"
            DRAW_ATTENTION -> "Draw Attention"
        }

    val description: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Always make double positive strain rolls to move faster when encumbered."
            IMPROVED_HEALING -> "Require no treatment to heal beyond 1 HP from negative value."
            PROTECT_ALLY -> "Choose a party member at session start. When protecting them, use one free combat die for the roll."
            RESIST_CURSES -> "+4 saving throw vs. cursed objects and may use combat dice to reduce cursed HP costs."
            DRAW_ATTENTION -> "Enemies choose to attack someone other than you first at the start of battle (if possible). Can be inverted when desired."
        }

    companion object {
        fun fromId(id: Int): CleverKnack? = values().find { it.id == id }
    }
}

object CleverKnackSerializer : KSerializer<CleverKnack> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CleverKnack", PrimitiveKind.INT)
    
    override fun serialize(encoder: Encoder, value: CleverKnack) {
        encoder.encodeInt(value.id)
    }
    
    override fun deserialize(decoder: Decoder): CleverKnack {
        val id = decoder.decodeInt()
        return CleverKnack.fromId(id) ?: CleverKnack.DOUBLE_STRAIN_ROLLS
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
    val slots: List<CleverKnackSlot> = List(10) { CleverKnackSlot() },
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
        // Ensure list size remains the same
        while (newSlots.size < 10) {
            newSlots.add(CleverKnackSlot())
        }
        return copy(knackOptions = CleverKnackOptions(slots = newSlots))
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
