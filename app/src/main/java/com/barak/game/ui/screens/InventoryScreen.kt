package com.barak.game.ui.screens

import androidx.compose.foundation.background
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
import com.barak.game.data.GearId
import com.barak.game.data.PlayerState
import com.barak.game.data.TattooId
import com.barak.game.ui.components.AssetImage
import com.barak.game.ui.components.BarakButton
import com.barak.game.ui.components.PanelCard
import com.barak.game.ui.components.ResourceBar
import com.barak.game.ui.theme.Beige
import com.barak.game.ui.theme.Ink
import com.barak.game.ui.theme.Muted

@Composable
fun InventoryScreen(
    player: PlayerState,
    onEquip: (GearId) -> Unit,
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
            Text("Шмот", color = Ink)
            Text("Дроп с боссов: куртка, маска, заточка, самопал, яд…", color = Muted)
            AssetImage("concept/barak-raid-items-icons.png", Modifier.fillMaxWidth().height(220.dp))
            if (player.inventory.isEmpty()) {
                PanelCard("Пусто", "Сходи в рейд — шмот падает с боссов.")
            } else {
                player.inventory.forEach { gear ->
                    val equipped = player.equipped[gear.slot] == gear
                    BarakButton(
                        text = if (equipped) "${gear.title} (на вас)" else "Надеть: ${gear.title}",
                        onClick = { onEquip(gear) },
                        enabled = !equipped,
                    )
                }
            }
            BarakButton("Назад", onBack)
        }
    }
}

@Composable
fun TattooScreen(
    player: PlayerState,
    onBuy: (TattooId) -> Unit,
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
            Text("Кольщик", color = Ink)
            AssetImage("concept/barak-loc3-kolschik.png", Modifier.fillMaxWidth().height(240.dp))
            TattooId.entries.forEach { tattoo ->
                val owned = tattoo in player.tattoos
                BarakButton(
                    text = if (owned) {
                        "${tattoo.title} ✓"
                    } else {
                        "Набить ${tattoo.title} — ${tattoo.cost} пап."
                    },
                    onClick = { onBuy(tattoo) },
                    enabled = !owned && player.cigarettes >= tattoo.cost,
                )
            }
            BarakButton("Назад", onBack)
        }
    }
}
