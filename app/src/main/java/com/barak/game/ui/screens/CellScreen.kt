package com.barak.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.unit.dp
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
            Text("Камера", color = Ink)
            Text(ZoneLocation.CELL.description, color = Muted)
            AssetImage(
                path = ZoneLocation.CELL.asset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
            )
            PanelCard(
                title = player.name,
                body = "Авторитет: ${player.authority}\n" +
                    "Энергия восстанавливается со временем.\n" +
                    "Рейды, шмот и наколки добавим позже.",
            )
            BarakButton("Отдохнуть (+20 эн. / 2 пап.)", onRest)
            BarakButton("Карта зоны", onOpenMap)
            Spacer(Modifier.height(24.dp))
        }
    }
}
