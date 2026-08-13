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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.barak.game.data.BossStage
import com.barak.game.data.GameContent
import com.barak.game.data.PlayerState
import com.barak.game.data.bossStage
import com.barak.game.ui.components.AssetImage
import com.barak.game.ui.components.BarakButton
import com.barak.game.ui.components.PanelCard
import com.barak.game.ui.components.ResourceBar
import com.barak.game.ui.theme.Accent
import com.barak.game.ui.theme.Beige
import com.barak.game.ui.theme.Danger
import com.barak.game.ui.theme.Ink
import com.barak.game.ui.theme.Muted
import com.barak.game.ui.theme.Ok

@Composable
fun RaidScreen(
    player: PlayerState,
    onStart: (String) -> Unit,
    onHit: () -> Unit,
    onBack: () -> Unit,
) {
    val activeBoss = GameContent.bosses.find { it.id == player.activeBossId }
    val stage = if (activeBoss != null) {
        bossStage(player.activeBossHp, activeBoss.maxHp)
    } else {
        null
    }

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
            Text("Рейд", color = Ink)
            Text("Тюремная одежда, заточка / самопал / яд с боссов", color = Muted)

            val stageAsset = when (stage) {
                BossStage.FULL -> "concept/barak-boss-stage1-full.png"
                BossStage.MID -> "concept/barak-boss-stage2-mid.png"
                BossStage.LOW, BossStage.DEAD -> "concept/barak-boss-stage3-low.png"
                null -> "concept/barak-raid-prison-v3.png"
            }
            AssetImage(stageAsset, Modifier.fillMaxWidth().height(320.dp))

            if (activeBoss != null && stage != null && stage != BossStage.DEAD) {
                val ratio = player.activeBossHp.toFloat() / activeBoss.maxHp.toFloat()
                Text("${activeBoss.name} — ${stageTitle(stage)}", color = Ink)
                LinearProgressIndicator(
                    progress = { ratio },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = when (stage) {
                        BossStage.FULL -> Ok
                        BossStage.MID -> Accent
                        else -> Danger
                    },
                    strokeCap = StrokeCap.Round,
                )
                Text("HP ${player.activeBossHp} / ${activeBoss.maxHp}", color = Muted)
                BarakButton("Бить", onHit)
            } else {
                PanelCard(
                    title = "Выбери босса",
                    body = "Чем серьёзнее босс — тем лучше камера и дроп шмота.",
                )
                GameContent.bosses.forEach { boss ->
                    val done = boss.id in player.defeatedBosses
                    BarakButton(
                        text = buildString {
                            append(boss.name)
                            append(" · HP ${boss.maxHp}")
                            append(" · камера ${boss.unlocksCell.title}")
                            if (done) append(" ✓")
                        },
                        onClick = { onStart(boss.id) },
                        enabled = player.energy >= boss.energyCost,
                    )
                }
            }

            BarakButton("Назад в камеру", onBack)
        }
    }
}

private fun stageTitle(stage: BossStage): String = when (stage) {
    BossStage.FULL -> "целый"
    BossStage.MID -> "побитый"
    BossStage.LOW -> "избитый"
    BossStage.DEAD -> "повержен"
}
