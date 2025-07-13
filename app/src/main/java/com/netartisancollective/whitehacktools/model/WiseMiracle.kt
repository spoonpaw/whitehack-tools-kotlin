package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class WiseMiracle(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val isActive: Boolean = false,
    val isAdditional: Boolean = false
)

@Serializable
data class WiseMiracleSlot(
    val id: String = UUID.randomUUID().toString(),
    val baseMiracles: List<WiseMiracle> = emptyList(),
    val additionalMiracles: List<WiseMiracle> = emptyList(),
    val additionalMiracleCount: Int = 0,
    val isMagicItemSlot: Boolean = false,
    val magicItemName: String = ""
) {
    init {
        android.util.Log.d("WiseMiracleSlot", "Creating slot with ${baseMiracles.size} base miracles and ${additionalMiracles.size} additional miracles")
        baseMiracles.forEach { miracle ->
            android.util.Log.d("WiseMiracleSlot", "Base miracle: ${miracle.name} (active=${miracle.isActive})")
        }
        additionalMiracles.forEach { miracle ->
            android.util.Log.d("WiseMiracleSlot", "Additional miracle: ${miracle.name} (active=${miracle.isActive})")
        }
    }

    val miracles: List<WiseMiracle>
        get() = baseMiracles + additionalMiracles
}

@Serializable
data class WiseMiracles(
    val slots: List<WiseMiracleSlot> = emptyList()
)
