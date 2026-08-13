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
import androidx.compose.ui.Modifier
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
fun AssetImage(
    path: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val bitmap = remember(path) {
        runCatching {
            context.assets.open(path).use(BitmapFactory::decodeStream)
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
        Box(modifier = modifier.background(Panel))
    }
}

@Composable
fun ResourceBar(player: PlayerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel)
            .border(width = 1.dp, color = Border)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = "БАРАК", color = Accent)
        Text(text = "Пап. ${player.cigarettes}", color = Ink)
        Text(text = "Эн. ${player.energy}/${player.maxEnergy}", color = Ink)
        Text(text = player.location.title, color = Muted)
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
        colors = ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Panel,
        ),
    ) {
        Text(text = text)
    }
}

@Composable
fun PanelCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Panel, shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Border, shape = RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = title, color = Ink)
        Text(text = body, color = Muted)
    }
}
