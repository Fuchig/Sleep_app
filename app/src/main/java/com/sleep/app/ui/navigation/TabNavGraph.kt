package com.sleep.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sleep.app.ui.screens.HistoryScreen
import com.sleep.app.ui.screens.ProfileScreen
import com.sleep.app.ui.screens.TimerScreen

@Composable
fun TabNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Timer.route,
        modifier = modifier
    ) {
        composable(Screen.Timer.route) {
            TimerScreen(
                onNavigateToHistory = {} // No longer needed with tabs
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = {} // No longer needed with tabs
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
