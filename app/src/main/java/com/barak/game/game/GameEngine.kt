package com.barak.game.game

import com.barak.game.data.Boss
import com.barak.game.data.BossStage
import com.barak.game.data.CellTier
import com.barak.game.data.GameContent
import com.barak.game.data.GearId
import com.barak.game.data.PlayerState
import com.barak.game.data.TattooId
import com.barak.game.data.bossStage
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

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

    fun rest(state: PlayerState): Pair<PlayerState, ActionResult> {
        if (state.cigarettes < 2) {
            return state to ActionResult("Мало папирос на отдых", false)
        }
        return state.copy(
            energy = min(state.maxEnergy, state.energy + 20),
            cigarettes = state.cigarettes - 2,
        ) to ActionResult("Отдохнул. +20 энергии")
    }

    fun startRaid(state: PlayerState, bossId: String): Pair<PlayerState, ActionResult> {
        val boss = GameContent.bosses.find { it.id == bossId }
            ?: return state to ActionResult("Босс не найден", false)
        if (state.energy < boss.energyCost) {
            return state to ActionResult("Мало энергии", false)
        }
        return state.copy(
            energy = state.energy - boss.energyCost,
            activeBossId = boss.id,
            activeBossHp = boss.maxHp,
        ) to ActionResult("Рейд начат: ${boss.name}")
    }

    fun hitBoss(state: PlayerState): Pair<PlayerState, ActionResult> {
        val bossId = state.activeBossId ?: return state to ActionResult("Нет активного рейда", false)
        val boss = GameContent.bosses.find { it.id == bossId }
            ?: return state to ActionResult("Босс не найден", false)

        val damage = (state.strength * (0.8 + Random.nextDouble() * 0.5)).toLong()
            .coerceAtLeast(1) + equippedHandBonus(state)
        val newHp = max(0L, state.activeBossHp - damage)
        val stage = bossStage(newHp, boss.maxHp)

        if (newHp > 0L) {
            return state.copy(activeBossHp = newHp) to ActionResult(
                "Удар на $damage. Стадия: ${stageLabel(stage)}. HP ${newHp}/${boss.maxHp}"
            )
        }

        return finishBoss(state, boss, damage)
    }

    private fun finishBoss(state: PlayerState, boss: Boss, lastHit: Long): Pair<PlayerState, ActionResult> {
        val drop = boss.drops.random()
        val unlocked = state.unlockedCells + boss.unlocksCell
        val next = state.copy(
            activeBossId = null,
            activeBossHp = 0,
            cigarettes = state.cigarettes + boss.cigaretteReward,
            authority = state.authority + boss.authorityReward,
            inventory = state.inventory + drop,
            unlockedCells = unlocked,
            cell = if (boss.unlocksCell.ordinal >= state.cell.ordinal) boss.unlocksCell else state.cell,
            defeatedBosses = state.defeatedBosses + boss.id,
            wins = state.wins + 1,
            experience = state.experience + 20,
            strength = state.strength + 1,
        )
        return next to ActionResult(
            "Босс повержен! +${boss.cigaretteReward} пап., +${boss.authorityReward} авт., дроп: ${drop.title}, камера: ${boss.unlocksCell.title}. Добивающий удар $lastHit"
        )
    }

    fun equip(state: PlayerState, gear: GearId): Pair<PlayerState, ActionResult> {
        if (gear !in state.inventory) {
            return state to ActionResult("Нет в инвентаре", false)
        }
        val equipped = state.equipped.toMutableMap()
        equipped[gear.slot] = gear
        return state.copy(equipped = equipped) to ActionResult("Надето: ${gear.title}")
    }

    fun buyTattoo(state: PlayerState, tattoo: TattooId): Pair<PlayerState, ActionResult> {
        if (tattoo in state.tattoos) {
            return state to ActionResult("Уже набито", false)
        }
        if (state.cigarettes < tattoo.cost) {
            return state to ActionResult("Мало папирос", false)
        }
        return state.copy(
            cigarettes = state.cigarettes - tattoo.cost,
            tattoos = state.tattoos + tattoo,
            mastery = state.mastery + 3,
            authority = state.authority + 2,
        ) to ActionResult("Набита наколка: ${tattoo.title}")
    }

    fun changeCell(state: PlayerState, cell: CellTier): Pair<PlayerState, ActionResult> {
        if (cell !in state.unlockedCells) {
            return state to ActionResult("Камера ещё не открыта. Бей боссов.", false)
        }
        return state.copy(cell = cell) to ActionResult("Переехал: ${cell.title}")
    }

    private fun equippedHandBonus(state: PlayerState): Long {
        return when (state.equipped[com.barak.game.data.GearSlot.HAND]) {
            GearId.ZATOCHKA -> 8
            GearId.BUTTERFLY -> 12
            GearId.SAMOPAL -> 25
            GearId.POISON -> 18
            else -> 0
        }
    }

    private fun stageLabel(stage: BossStage): String = when (stage) {
        BossStage.FULL -> "целый"
        BossStage.MID -> "побитый"
        BossStage.LOW -> "избитый"
        BossStage.DEAD -> "повержен"
    }
}
