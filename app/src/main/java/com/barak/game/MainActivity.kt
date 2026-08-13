package com.barak.game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barak.game.data.ZoneLocation
import com.barak.game.game.GameViewModel
import com.barak.game.ui.screens.CellScreen
import com.barak.game.ui.screens.LocationScreen
import com.barak.game.ui.screens.MapScreen
import com.barak.game.ui.theme.BarakTheme
import com.barak.game.ui.theme.Beige

class MainActivity : ComponentActivity() {
    private val vm: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarakTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Beige) {
                    val ui by vm.ui.collectAsStateWithLifecycle()
                    val nav = rememberNavController()

                    LaunchedEffect(ui.toast) {
                        val msg = ui.toast ?: return@LaunchedEffect
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        vm.clearToast()
                    }

                    NavHost(navController = nav, startDestination = "home") {
                        composable("home") {
                            if (ui.player.location == ZoneLocation.CELL) {
                                CellScreen(
                                    player = ui.player,
                                    onRest = vm::rest,
                                    onOpenMap = { nav.navigate("map") },
                                )
                            } else {
                                LocationScreen(
                                    player = ui.player,
                                    onBackToCell = {
                                        vm.goTo(ZoneLocation.CELL)
                                        nav.popBackStack("home", inclusive = false)
                                    },
                                    onOpenMap = { nav.navigate("map") },
                                )
                            }
                        }
                        composable("map") {
                            MapScreen(
                                player = ui.player,
                                onOpen = { loc ->
                                    vm.goTo(loc)
                                    nav.popBackStack("home", inclusive = false)
                                },
                                onBack = { nav.popBackStack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
