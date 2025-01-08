package com.example.whitehacktools.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.whitehacktools.model.PlayerCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class CharacterStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "characters")
        private val CHARACTERS_KEY = stringPreferencesKey("characters")
    }

    val characters: Flow<List<PlayerCharacter>> = context.dataStore.data
        .map { preferences ->
            val charactersJson = preferences[CHARACTERS_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<PlayerCharacter>>(charactersJson)
                    .map { it.fixOutOfRangeAttributes() }
                    .map { it.validateWiseMiracles() }
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun saveCharacters(characters: List<PlayerCharacter>) {
        context.dataStore.edit { preferences ->
            preferences[CHARACTERS_KEY] = Json.encodeToString(
                characters
                    .map { it.fixOutOfRangeAttributes() }
                    .map { it.validateWiseMiracles() }
            )
        }
    }
}

private fun PlayerCharacter.validateWiseMiracles(): PlayerCharacter {
    val validatedSlots = wiseMiracleSlots.map { slot ->
        if (!slot.isMagicItemSlot) {
            var activeFound = false
            val validatedBaseMiracles = slot.baseMiracles.map { miracle ->
                if (miracle.isActive) {
                    if (activeFound) {
                        miracle.copy(isActive = false)
                    } else {
                        activeFound = true
                        miracle
                    }
                } else miracle
            }

            val validatedAdditionalMiracles = slot.additionalMiracles.map { miracle ->
                if (miracle.isActive) {
                    if (activeFound) {
                        miracle.copy(isActive = false)
                    } else {
                        activeFound = true
                        miracle
                    }
                } else miracle
            }

            slot.copy(
                baseMiracles = validatedBaseMiracles,
                additionalMiracles = validatedAdditionalMiracles
            )
        } else slot
    }

    return if (validatedSlots != wiseMiracleSlots) {
        copy(wiseMiracleSlots = validatedSlots)
    } else this
}
