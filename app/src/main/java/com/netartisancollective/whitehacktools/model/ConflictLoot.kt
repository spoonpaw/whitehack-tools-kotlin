package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable

@Serializable
data class ConflictLoot(
    val keyword: String = "",
    val usesRemaining: Int = 0,
    val type: ConflictLootType = ConflictLootType.Special,
    val description: String = ""
)
