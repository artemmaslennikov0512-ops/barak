package com.barak.game.data

/**
 * Базовые локации зоны. Шмот/рейды/наколки добавим позже.
 */
enum class ZoneLocation(
    val title: String,
    val description: String,
    val asset: String,
) {
    CELL(
        title = "Камера",
        description = "Твоя хата. Здесь отдыхаешь и смотришь прогресс.",
        asset = "concept/barak-loc3-cell.png",
    ),
    YARD(
        title = "Двор",
        description = "Прогулка, разборки и слухи зоны.",
        asset = "concept/barak-loc3-dvor.png",
    ),
    CANTEEN(
        title = "Столовка",
        description = "Баланда, чай и короткие разговоры.",
        asset = "concept/barak-loc3-stolovka.png",
    ),
    BATH(
        title = "Баня",
        description = "Пар, вода и редкий покой.",
        asset = "concept/barak-loc3-banya.png",
    ),
    TATTOOIST(
        title = "Кольщик",
        description = "Здесь потом появятся наколки.",
        asset = "concept/barak-loc3-kolschik.png",
    ),
    WORKSHOP(
        title = "Мастерская",
        description = "Крафт и поручения. Пока заготовка.",
        asset = "concept/barak-loc3-masterskaya.png",
    ),
    BRIGADE(
        title = "Штаб бригады",
        description = "Свои люди. Контент позже.",
        asset = "concept/barak-loc3-brigade.png",
    ),
    SHIZO(
        title = "ШИЗО",
        description = "Карцер. Лучше сюда не попадать.",
        asset = "concept/barak-loc3-shizo.png",
    ),
    GATE(
        title = "Проходная",
        description = "Вход и выход зоны.",
        asset = "concept/barak-loc3-prohodnaya.png",
    ),
}

data class PlayerState(
    val name: String = "Зек",
    val cigarettes: Int = 40,
    val energy: Int = 100,
    val maxEnergy: Int = 100,
    val authority: Int = 0,
    val location: ZoneLocation = ZoneLocation.CELL,
    val lastTickAt: Long = System.currentTimeMillis(),
)
