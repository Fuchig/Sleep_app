package com.sleep.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sleep.app.ui.screens.HistoryScreen
import com.sleep.app.ui.screens.TabScreen
import com.sleep.app.ui.screens.TimerScreen

sealed class Screen(val route: String) {
    object Timer : Screen("timer")
    object History : Screen("history")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "tab_screen"
    ) {
        composable("tab_screen") {
            TabScreen()
        }
    }
}
