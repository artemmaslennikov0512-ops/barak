# БАРАК — Android

Основа игры: **камера + локации зоны**.  
Kotlin + Jetpack Compose. Открывается в Android Studio.

## Запуск

1. Android Studio → **File → Open** → эта папка  
2. Gradle Sync  
3. Run на эмулятор / телефон  

Или клон:
```bash
git clone https://github.com/artemmaslennikov0512-ops/barak.git
```

## Сейчас есть

- Камера (главный экран, отдых)
- Карта зоны и переходы:
  - Двор, Столовка, Баня, Кольщик, Мастерская, Штаб, ШИЗО, Проходная
- Папиросы / энергия + idle-восстановление
- Сохранение прогресса

## Позже (по факту)

- Рейды и стадии боссов
- Шмот и дроп
- Наколки
- Действия внутри локаций

## Структура

```
app/src/main/java/com/barak/game/
  data/          # локации и игрок
  game/          # логика + сохранение
  ui/screens/    # камера, карта, локация
  MainActivity.kt
app/src/main/assets/concept/  # арты локаций
```
