package com.barak.game.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.barak.game.data.CellTier
import com.barak.game.data.GearId
import com.barak.game.data.PlayerState
import com.barak.game.data.TattooId
import com.barak.game.data.ZoneLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val player: PlayerState = PlayerState(),
    val location: ZoneLocation = ZoneLocation.CELL,
    val toast: String? = null,
)

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val store = SaveStore(app)

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = store.load() ?: PlayerState()
            _ui.update { it.copy(player = GameEngine.applyIdle(loaded)) }
        }
    }

    private fun mutate(block: (PlayerState) -> Pair<PlayerState, com.barak.game.game.ActionResult>) {
        _ui.update { current ->
            val (next, result) = block(current.player)
            viewModelScope.launch { store.save(next) }
            current.copy(player = next, toast = result.message)
        }
    }

    fun clearToast() = _ui.update { it.copy(toast = null) }

    fun openLocation(location: ZoneLocation) {
        _ui.update { it.copy(location = location) }
    }

    fun rest() = mutate(GameEngine::rest)

    fun startRaid(bossId: String) = mutate { GameEngine.startRaid(it, bossId) }

    fun hitBoss() = mutate(GameEngine::hitBoss)

    fun equip(gear: GearId) = mutate { GameEngine.equip(it, gear) }

    fun buyTattoo(tattoo: TattooId) = mutate { GameEngine.buyTattoo(it, tattoo) }

    fun changeCell(cell: CellTier) = mutate { GameEngine.changeCell(it, cell) }
}
