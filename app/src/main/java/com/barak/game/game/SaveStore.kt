package com.barak.game.game

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.barak.game.data.CellTier
import com.barak.game.data.GearId
import com.barak.game.data.GearSlot
import com.barak.game.data.PlayerState
import com.barak.game.data.TattooId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore("barak_save")

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
        val json = JSONObject()
        json.put("name", state.name)
        json.put("level", state.level)
        json.put("experience", state.experience)
        json.put("strength", state.strength)
        json.put("authority", state.authority)
        json.put("cigarettes", state.cigarettes)
        json.put("energy", state.energy)
        json.put("maxEnergy", state.maxEnergy)
        json.put("mastery", state.mastery)
        json.put("cell", state.cell.name)
        json.put("unlockedCells", JSONArray(state.unlockedCells.map { it.name }))
        json.put("inventory", JSONArray(state.inventory.map { it.name }))
        val equipped = JSONObject()
        state.equipped.forEach { (slot, gear) -> equipped.put(slot.name, gear.name) }
        json.put("equipped", equipped)
        json.put("tattoos", JSONArray(state.tattoos.map { it.name }))
        json.put("defeatedBosses", JSONArray(state.defeatedBosses.toList()))
        json.put("activeBossId", state.activeBossId)
        json.put("activeBossHp", state.activeBossHp)
        json.put("wins", state.wins)
        json.put("losses", state.losses)
        json.put("lastTickAt", state.lastTickAt)
        return json.toString()
    }

    private fun decode(raw: String): PlayerState {
        val json = JSONObject(raw)
        val unlocked = json.optJSONArray("unlockedCells").toEnumSet(CellTier.PENALTY) { CellTier.valueOf(it) }
        val inventoryReal = buildSet {
            val arr = json.optJSONArray("inventory") ?: JSONArray()
            for (i in 0 until arr.length()) add(GearId.valueOf(arr.getString(i)))
        }
        val tattoos = buildSet {
            val arr = json.optJSONArray("tattoos") ?: JSONArray()
            for (i in 0 until arr.length()) add(TattooId.valueOf(arr.getString(i)))
        }
        val defeated = buildSet {
            val arr = json.optJSONArray("defeatedBosses") ?: JSONArray()
            for (i in 0 until arr.length()) add(arr.getString(i))
        }
        val equippedJson = json.optJSONObject("equipped") ?: JSONObject()
        val equipped = buildMap {
            equippedJson.keys().forEach { key ->
                put(GearSlot.valueOf(key), GearId.valueOf(equippedJson.getString(key)))
            }
        }
        return PlayerState(
            name = json.optString("name", "Зек"),
            level = json.optInt("level", 1),
            experience = json.optInt("experience", 0),
            strength = json.optInt("strength", 10),
            authority = json.optInt("authority", 0),
            cigarettes = json.optInt("cigarettes", 40),
            energy = json.optInt("energy", 100),
            maxEnergy = json.optInt("maxEnergy", 100),
            mastery = json.optInt("mastery", 0),
            cell = CellTier.valueOf(json.optString("cell", CellTier.PENALTY.name)),
            unlockedCells = unlocked.ifEmpty { setOf(CellTier.PENALTY) },
            inventory = inventoryReal,
            equipped = equipped,
            tattoos = tattoos,
            defeatedBosses = defeated,
            activeBossId = json.optString("activeBossId", "").ifBlank { null },
            activeBossHp = json.optLong("activeBossHp", 0),
            wins = json.optInt("wins", 0),
            losses = json.optInt("losses", 0),
            lastTickAt = json.optLong("lastTickAt", System.currentTimeMillis()),
        )
    }

    private fun <T> JSONArray?.toEnumSet(fallback: T, parse: (String) -> T): Set<T> {
        if (this == null) return setOf(fallback)
        return buildSet {
            for (i in 0 until length()) add(parse(getString(i)))
        }
    }
}
