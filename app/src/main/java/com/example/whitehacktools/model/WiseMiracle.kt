package com.example.whitehacktools.model

import kotlinx.serialization.Serializable

@Serializable
data class WiseMiracle(
    val name: String = "",
    val isActive: Boolean = false
)

@Serializable
data class WiseMiracleSlot(
    val miracles: List<WiseMiracle> = emptyList(),
    val additionalMiracleCount: Int = 0,
    val isMagicItemSlot: Boolean = false,
    val magicItemName: String = ""
)

@Serializable
data class WiseMiracles(
    val slots: List<WiseMiracleSlot> = emptyList()
)
