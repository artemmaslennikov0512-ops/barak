package com.barak.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barak.game.data.CellTier
import com.barak.game.data.PlayerState
import com.barak.game.data.ZoneLocation
import com.barak.game.ui.components.AssetImage
import com.barak.game.ui.components.BarakButton
import com.barak.game.ui.components.PanelCard
import com.barak.game.ui.components.ResourceBar
import com.barak.game.ui.theme.Beige
import com.barak.game.ui.theme.Ink
import com.barak.game.ui.theme.Muted

@Composable
fun CellScreen(
    player: PlayerState,
    onRest: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenRaid: () -> Unit,
    onOpenInventory: () -> Unit,
    onOpenTattoos: () -> Unit,
    onChangeCell: (CellTier) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige),
    ) {
        ResourceBar(player)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Твоя камера", color = Ink)
            Text(player.cell.title, color = Muted)
            Box {
                AssetImage(
                    path = ZoneLocation.CELL.asset,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                )
            }
            PanelCard(
                title = player.name,
                body = "Камера: ${player.cell.title}\n${player.cell.privileges}\n" +
                    "Авторитет ${player.authority} · Побед ${player.wins}\n" +
                    "Наколки: ${if (player.tattoos.isEmpty()) "нет" else player.tattoos.joinToString { it.title }}\n" +
                    "В руках: ${player.equipped.values.joinToString { it.title }.ifBlank { "пусто" }}",
            )
            BarakButton("Отдохнуть (+20 эн. / 2 пап.)", onRest)
            BarakButton("Карта зоны", onOpenMap)
            BarakButton("Рейд на босса", onOpenRaid)
            BarakButton("Шмот", onOpenInventory)
            BarakButton("Кольщик / наколки", onOpenTattoos)
            Text("Сменить камеру", color = Ink)
            CellTier.entries.forEach { tier ->
                val unlocked = tier in player.unlockedCells
                BarakButton(
                    text = if (unlocked) tier.title else "${tier.title} (закрыто)",
                    onClick = { onChangeCell(tier) },
                    enabled = unlocked,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
