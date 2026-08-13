package com.barak.game.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barak.game.data.PlayerState
import com.barak.game.ui.theme.Accent
import com.barak.game.ui.theme.Border
import com.barak.game.ui.theme.Ink
import com.barak.game.ui.theme.Muted
import com.barak.game.ui.theme.Panel

@Composable
fun AssetImage(path: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember(path) {
        runCatching {
            context.assets.open(path).use { BitmapFactory.decodeStream(it) }
        }.getOrNull()
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        Box(modifier.background(Panel))
    }
}

@Composable
fun ResourceBar(player: PlayerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel)
            .border(1.dp, Border)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("БАРАК", color = Accent)
        Text("Пап. ${player.cigarettes}", color = Ink)
        Text("Эн. ${player.energy}/${player.maxEnergy}", color = Ink)
        Text(player.location.title, color = Muted)
    }
}

@Composable
fun BarakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Panel),
    ) {
        Text(text)
    }
}

@Composable
fun PanelCard(title: String, body: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(12.dp))
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(title, color = Ink)
        Text(body, color = Muted)
    }
}
