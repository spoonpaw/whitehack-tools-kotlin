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
    val additionalMiracleCount: Int = 0
)

@Serializable
data class WiseMiracles(
    val slots: List<WiseMiracleSlot> = emptyList()
)
