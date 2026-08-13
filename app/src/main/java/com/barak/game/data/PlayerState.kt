package com.barak.game.data

data class PlayerState(
    val name: String = "Зек",
    val level: Int = 1,
    val experience: Int = 0,
    val strength: Int = 10,
    val authority: Int = 0,
    val cigarettes: Int = 40,
    val energy: Int = 100,
    val maxEnergy: Int = 100,
    val mastery: Int = 0,
    val cell: CellTier = CellTier.PENALTY,
    val unlockedCells: Set<CellTier> = setOf(CellTier.PENALTY),
    val inventory: Set<GearId> = emptySet(),
    val equipped: Map<GearSlot, GearId> = emptyMap(),
    val tattoos: Set<TattooId> = emptySet(),
    val defeatedBosses: Set<String> = emptySet(),
    val activeBossId: String? = null,
    val activeBossHp: Long = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val lastTickAt: Long = System.currentTimeMillis(),
)

enum class BossStage {
    FULL,
    MID,
    LOW,
    DEAD,
}

fun bossStage(hp: Long, maxHp: Long): BossStage {
    if (hp <= 0L) return BossStage.DEAD
    val ratio = hp.toDouble() / maxHp.toDouble()
    return when {
        ratio > 0.55 -> BossStage.FULL
        ratio > 0.18 -> BossStage.MID
        else -> BossStage.LOW
    }
}
