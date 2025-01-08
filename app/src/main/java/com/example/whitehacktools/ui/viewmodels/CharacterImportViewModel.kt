package com.example.whitehacktools.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitehacktools.model.PlayerCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CharacterImportViewModel : ViewModel() {
    private val _importError = MutableStateFlow<String?>(null)
    val importError: StateFlow<String?> = _importError.asStateFlow()

    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
    }

    fun importCharacter(uri: Uri): PlayerCharacter? {
        return try {
            val jsonString = uri.toString()
            json.decodeFromString<PlayerCharacter>(jsonString)
        } catch (e: Exception) {
            _importError.value = "Failed to import character: ${e.message}"
            null
        }
    }

    fun exportCharacter(character: PlayerCharacter, file: File) {
        viewModelScope.launch {
            try {
                val jsonString = json.encodeToString(character)
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: IOException) {
                _importError.value = "Failed to export character: ${e.message}"
            }
        }
    }

    fun clearError() {
        _importError.value = null
    }
}
