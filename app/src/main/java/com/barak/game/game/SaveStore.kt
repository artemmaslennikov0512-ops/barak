package com.barak.game.game

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.barak.game.data.PlayerState
import com.barak.game.data.ZoneLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore("barak_save_v2")

class SaveStore(private val context: Context) {
    private val key = stringPreferencesKey("player_json")

    suspend fun save(state: PlayerState) {
        context.dataStore.edit { prefs ->
            prefs[key] = encode(state)
        }
    }

    suspend fun load(): PlayerState? {
        val raw = context.dataStore.data.map { it[key] }.first() ?: return null
        return runCatching { decode(raw) }.getOrNull()
    }

    private fun encode(state: PlayerState): String {
        return JSONObject()
            .put("name", state.name)
            .put("cigarettes", state.cigarettes)
            .put("energy", state.energy)
            .put("maxEnergy", state.maxEnergy)
            .put("authority", state.authority)
            .put("location", state.location.name)
            .put("lastTickAt", state.lastTickAt)
            .toString()
    }

    private fun decode(raw: String): PlayerState {
        val json = JSONObject(raw)
        return PlayerState(
            name = json.optString("name", "Зек"),
            cigarettes = json.optInt("cigarettes", 40),
            energy = json.optInt("energy", 100),
            maxEnergy = json.optInt("maxEnergy", 100),
            authority = json.optInt("authority", 0),
            location = runCatching {
                ZoneLocation.valueOf(json.optString("location", ZoneLocation.CELL.name))
            }.getOrDefault(ZoneLocation.CELL),
            lastTickAt = json.optLong("lastTickAt", System.currentTimeMillis()),
        )
    }
}
