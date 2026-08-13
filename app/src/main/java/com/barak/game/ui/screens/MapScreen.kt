package com.barak.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.barak.game.data.ZoneLocation
import com.barak.game.ui.components.AssetImage
import com.barak.game.ui.components.BarakButton
import com.barak.game.ui.components.ResourceBar
import com.barak.game.data.PlayerState
import com.barak.game.ui.theme.Beige
import com.barak.game.ui.theme.Ink
import com.barak.game.ui.theme.Muted

@Composable
fun MapScreen(
    player: PlayerState,
    current: ZoneLocation,
    onOpen: (ZoneLocation) -> Unit,
    onBack: () -> Unit,
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
            Text("Карта зоны", color = Ink)
            Text("Сейчас: ${current.title}", color = Muted)
            ZoneLocation.entries.forEach { loc ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(loc) },
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(loc.title, color = Ink)
                    AssetImage(loc.asset, Modifier.fillMaxWidth().height(160.dp))
                }
            }
            BarakButton("Назад", onBack)
        }
    }
}

@Composable
fun LocationScreen(
    player: PlayerState,
    location: ZoneLocation,
    onBack: () -> Unit,
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
            Text(location.title, color = Ink)
            AssetImage(location.asset, Modifier.fillMaxWidth().height(360.dp))
            Text("Локация зоны. Позже здесь будут действия и NPC.", color = Muted)
            BarakButton("В камеру", onBack)
        }
    }
}
