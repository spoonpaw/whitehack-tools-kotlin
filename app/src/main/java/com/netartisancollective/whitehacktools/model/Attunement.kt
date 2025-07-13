package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable

enum class AttunementType {
    TEACHER,
    ITEM,
    VEHICLE,
    PET,
    PLACE
}

@Serializable
data class Attunement(
    val name: String = "",
    val type: AttunementType = AttunementType.ITEM,
    val isActive: Boolean = false,
    val isLost: Boolean = false
)
