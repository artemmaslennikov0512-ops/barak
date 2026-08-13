package com.barak.game.data

enum class CellTier(
    val title: String,
    val privileges: String,
    val asset: String,
) {
    PENALTY("Штрафная", "Привилегий нет", "concept/barak-loc3-cell.png"),
    STARTER("Обычная", "Своя шконка", "concept/barak-loc3-cell.png"),
    MID("С привилегиями", "Чайник, радио, занавеска", "concept/barak-loc3-cell.png"),
    AUTHORITY("Авторитет", "Ковёр, мебель, лампа", "concept/barak-loc3-cell.png"),
    BLATNOY("Блатная", "ТВ, холодильник, максимум", "concept/barak-loc3-cell.png"),
}

enum class ZoneLocation(
    val title: String,
    val asset: String,
) {
    CELL("Камера", "concept/barak-loc3-cell.png"),
    YARD("Двор", "concept/barak-loc3-dvor.png"),
    CANTEEN("Столовка", "concept/barak-loc3-stolovka.png"),
    BATH("Баня", "concept/barak-loc3-banya.png"),
    TATTOOIST("Кольщик", "concept/barak-loc3-kolschik.png"),
    WORKSHOP("Мастерская", "concept/barak-loc3-masterskaya.png"),
    BRIGADE("Штаб бригады", "concept/barak-loc3-brigade.png"),
    SHIZO("ШИЗО", "concept/barak-loc3-shizo.png"),
    GATE("Проходная", "concept/barak-loc3-prohodnaya.png"),
}

enum class GearId(val title: String, val slot: GearSlot) {
    JACKET("Куртка", GearSlot.BODY),
    PANTS("Штаны", GearSlot.LEGS),
    SHORTS("Шорты", GearSlot.LEGS),
    PSYCHO_MASK("Маска психопата", GearSlot.HEAD),
    CAP("Кепка", GearSlot.HEAD),
    CHETKI("Четки", GearSlot.HAND),
    BUTTERFLY("Нож-бабочка", GearSlot.HAND),
    ZATOCHKA("Заточка", GearSlot.HAND),
    SAMOPAL("Самопал", GearSlot.HAND),
    POISON("Яд", GearSlot.HAND),
}

enum class GearSlot { HEAD, BODY, LEGS, HAND }

enum class TattooId(val title: String, val cost: Int) {
    STARS("Звёзды", 40),
    SPIDER("Паук", 70),
    CHURCH("Храм", 120),
    FULL("Полный забив", 250),
}

data class Boss(
    val id: String,
    val name: String,
    val maxHp: Long,
    val energyCost: Int,
    val cigaretteReward: Int,
    val authorityReward: Int,
    val unlocksCell: CellTier,
    val drops: List<GearId>,
)

object GameContent {
    val bosses = listOf(
        Boss(
            id = "shnyr",
            name = "Шнырь",
            maxHp = 1_000,
            energyCost = 10,
            cigaretteReward = 25,
            authorityReward = 5,
            unlocksCell = CellTier.STARTER,
            drops = listOf(GearId.SHORTS, GearId.ZATOCHKA),
        ),
        Boss(
            id = "bespredel",
            name = "Беспредельщик",
            maxHp = 5_000,
            energyCost = 15,
            cigaretteReward = 60,
            authorityReward = 15,
            unlocksCell = CellTier.MID,
            drops = listOf(GearId.JACKET, GearId.BUTTERFLY),
        ),
        Boss(
            id = "vertuhay",
            name = "Вертухай",
            maxHp = 20_000,
            energyCost = 20,
            cigaretteReward = 120,
            authorityReward = 35,
            unlocksCell = CellTier.AUTHORITY,
            drops = listOf(GearId.PANTS, GearId.SAMOPAL, GearId.CAP),
        ),
        Boss(
            id = "smotryashiy",
            name = "Смотрящий",
            maxHp = 80_000,
            energyCost = 28,
            cigaretteReward = 250,
            authorityReward = 80,
            unlocksCell = CellTier.BLATNOY,
            drops = listOf(GearId.PSYCHO_MASK, GearId.POISON, GearId.CHETKI),
        ),
        Boss(
            id = "pahan",
            name = "Пахан",
            maxHp = 250_000,
            energyCost = 35,
            cigaretteReward = 500,
            authorityReward = 200,
            unlocksCell = CellTier.BLATNOY,
            drops = listOf(GearId.SAMOPAL, GearId.PSYCHO_MASK, GearId.JACKET),
        ),
    )
}
