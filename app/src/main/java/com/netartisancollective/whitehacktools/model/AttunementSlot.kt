package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable

@Serializable
data class AttunementSlot(
    val primaryAttunement: Attunement = Attunement(),
    val secondaryAttunement: Attunement = Attunement(),
    val tertiaryAttunement: Attunement = Attunement(),
    val quaternaryAttunement: Attunement = Attunement(),
    val hasTertiaryAttunement: Boolean = false,
    val hasQuaternaryAttunement: Boolean = false,
    val hasUsedDailyPower: Boolean = false
)
