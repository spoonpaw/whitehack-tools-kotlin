package com.example.whitehacktools.model

import com.example.whitehacktools.utilities.AdvancementTables
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable(with = BraveQuirkSerializer::class)
enum class BraveQuirk(val id: Int) {
    DOUBLE_STRAIN_ROLLS(0),
    IMPROVED_HEALING(1),
    PROTECT_ALLY(2),
    RESIST_CURSES(3),
    DRAW_ATTENTION(4),
    FULFILL_REQUIREMENTS(5),
    DIVINE_INVOCATION(6),
    IMPROVISED_WEAPONS(7);

    val displayName: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Double Strain Rolls"
            IMPROVED_HEALING -> "Improved Healing"
            PROTECT_ALLY -> "Protect Ally"
            RESIST_CURSES -> "Resist Curses"
            DRAW_ATTENTION -> "Draw Attention"
            FULFILL_REQUIREMENTS -> "Fulfill Requirements"
            DIVINE_INVOCATION -> "Divine Invocation"
            IMPROVISED_WEAPONS -> "Improvised Weapons"
        }

    val description: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Always make double positive strain rolls to move faster when encumbered."
            IMPROVED_HEALING -> "Require no treatment to heal beyond 1 HP from negative value. Can use comeback dice for damage shrugged off on successful save."
            PROTECT_ALLY -> "Choose a party member at session start. When protecting them, use one free comeback die for the roll."
            RESIST_CURSES -> "+4 saving throw vs. cursed objects and may use comeback dice to reduce cursed HP costs."
            DRAW_ATTENTION -> "Enemies choose to attack someone other than you first at the start of battle (if possible). Can be inverted when desired."
            FULFILL_REQUIREMENTS -> "Once per session, your courage fulfills user requirements that an item, place, or passage may have in the form of class or groups."
            DIVINE_INVOCATION -> "Once per session, use a god's name to halt, scare, convince, bless, or curse your level number of listeners (none higher level than you, +1 for holy symbol/affiliation). Each target may save to avoid."
            IMPROVISED_WEAPONS -> "Improvised weapons do at least 1d6 damage, and actual weapons ignore target resistances (but not immunities)."
        }

    companion object {
        val values = values()
        fun fromId(id: Int): BraveQuirk? = values.find { it.id == id }
    }
}

object BraveQuirkSerializer : KSerializer<BraveQuirk> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BraveQuirk", PrimitiveKind.INT)
    
    override fun serialize(encoder: Encoder, value: BraveQuirk) {
        encoder.encodeInt(value.id)
    }
    
    override fun deserialize(decoder: Decoder): BraveQuirk {
        val id = decoder.decodeInt()
        return BraveQuirk.fromId(id) ?: BraveQuirk.DOUBLE_STRAIN_ROLLS
    }
}

@Serializable
data class BraveQuirkSlot(
    @SerialName("quirk")
    val quirk: BraveQuirk? = null,
    @SerialName("protectedAllyName")
    val protectedAllyName: String = "", // Only used if quirk is PROTECT_ALLY
    @SerialName("id")
    val id: String = UUID.randomUUID().toString()
)

@Serializable
data class BraveQuirkOptions(
    @SerialName("slots")
    val slots: List<BraveQuirkSlot> = List(10) { BraveQuirkSlot() }
)

@Serializable
data class BraveAbilities(
    @SerialName("braveQuirkOptions")
    val quirkOptions: BraveQuirkOptions = BraveQuirkOptions(),
    @SerialName("comebackDice")
    val comebackDice: Int = 0,
    @SerialName("hasUsedSayNo")
    val hasUsedSayNo: Boolean = false
) {
    val activeQuirks: List<BraveQuirk>
        get() = quirkOptions.slots.mapNotNull { it.quirk }

    fun getQuirkSlot(at: Int): BraveQuirkSlot =
        if (at < quirkOptions.slots.size) quirkOptions.slots[at] else BraveQuirkSlot()

    fun setQuirkSlot(slot: BraveQuirkSlot, at: Int): BraveAbilities {
        if (at >= quirkOptions.slots.size) return this
        val newSlots = quirkOptions.slots.toMutableList()
        newSlots[at] = slot
        // Ensure list size remains the same
        while (newSlots.size < 10) {
            newSlots.add(BraveQuirkSlot())
        }
        return copy(quirkOptions = BraveQuirkOptions(slots = newSlots))
    }

    fun isQuirkActive(quirk: BraveQuirk): Boolean =
        quirkOptions.slots.any { it.quirk == quirk }

    fun isSlotFilled(at: Int): Boolean =
        at < quirkOptions.slots.size && quirkOptions.slots[at].quirk != null

    val count: Int
        get() = quirkOptions.slots.count { it.quirk != null }

    fun setQuirk(at: Int, quirk: BraveQuirk?, protectedAllyName: String = ""): BraveAbilities {
        return setQuirkSlot(BraveQuirkSlot(quirk = quirk, protectedAllyName = protectedAllyName), at)
    }

    fun updateForLevel(level: Int): BraveAbilities {
        val availableSlots = AdvancementTables.stats("Brave", level).slots
        val newSlots = quirkOptions.slots.take(availableSlots).toMutableList()
        while (newSlots.size < availableSlots) {
            newSlots.add(BraveQuirkSlot())
        }
        return copy(quirkOptions = BraveQuirkOptions(slots = newSlots))
    }
}
