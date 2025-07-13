package com.netartisancollective.whitehacktools.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.netartisancollective.whitehacktools.model.PlayerCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CharacterStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "characters")
        private val CHARACTERS_KEY = stringPreferencesKey("characters")
        private val json = Json {
            ignoreUnknownKeys = true  // Ignore fields we don't have in our model
            encodeDefaults = true     // Include default values in JSON
            coerceInputValues = true  // Try to coerce values to the right type
        }
    }

    val characters: Flow<List<PlayerCharacter>> = context.dataStore.data
        .map { preferences ->
            val charactersJson = preferences[CHARACTERS_KEY] ?: "[]"
            try {
                json.decodeFromString<List<PlayerCharacter>>(charactersJson)
                    .map { it.fixOutOfRangeAttributes() }
                    .map { it.validateWiseMiracles() }
                    .map { character ->
                        // If we have Swift format data but Kotlin format is empty, copy the data over
                        character.copy(
                            vocation = character.effectiveVocation,
                            species = character.effectiveSpecies,
                            affiliations = character.effectiveAffiliations,
                            // Clear Swift format fields after copying to avoid duplication
                            vocationGroup = null,
                            speciesGroup = null,
                            affiliationGroups = null
                        )
                    }
            } catch (e: Exception) {
                android.util.Log.e("CharacterStore", "Error decoding characters", e)
                emptyList()
            }
        }

    suspend fun saveCharacters(characters: List<PlayerCharacter>) {
        context.dataStore.edit { preferences ->
            preferences[CHARACTERS_KEY] = json.encodeToString(
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
