package com.barak.game.game

import com.barak.game.data.PlayerState
import com.barak.game.data.ZoneLocation
import kotlin.math.min

data class ActionResult(
    val message: String,
    val ok: Boolean = true,
)

object GameEngine {
    private const val ENERGY_PER_MIN = 3
    private const val CIG_PER_MIN = 1

    fun applyIdle(state: PlayerState, now: Long = System.currentTimeMillis()): PlayerState {
        val minutes = ((now - state.lastTickAt).coerceAtLeast(0L) / 60_000.0).coerceAtMost(8 * 60.0)
        if (minutes < 0.25) return state.copy(lastTickAt = now)
        val energyGain = (minutes * ENERGY_PER_MIN).toInt()
        val cigGain = (minutes * CIG_PER_MIN).toInt()
        return state.copy(
            energy = min(state.maxEnergy, state.energy + energyGain),
            cigarettes = state.cigarettes + cigGain,
            lastTickAt = now,
        )
    }

    fun goTo(state: PlayerState, location: ZoneLocation): Pair<PlayerState, ActionResult> {
        if (state.location == location) {
            return state to ActionResult("Уже здесь: ${location.title}")
        }
        if (state.energy < 2 && location != ZoneLocation.CELL) {
            return state to ActionResult("Мало энергии на переход", false)
        }
        val energyCost = if (location == ZoneLocation.CELL) 0 else 2
        return state.copy(
            location = location,
            energy = (state.energy - energyCost).coerceAtLeast(0),
        ) to ActionResult("Перешёл: ${location.title}")
    }

    fun rest(state: PlayerState): Pair<PlayerState, ActionResult> {
        if (state.location != ZoneLocation.CELL) {
            return state to ActionResult("Отдыхать можно в камере", false)
        }
        if (state.cigarettes < 2) {
            return state to ActionResult("Мало папирос", false)
        }
        return state.copy(
            energy = min(state.maxEnergy, state.energy + 20),
            cigarettes = state.cigarettes - 2,
        ) to ActionResult("Отдохнул. +20 энергии")
    }
}
