package com.sleep.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sleep.app.ui.navigation.Screen
import com.sleep.app.ui.navigation.TabNavGraph

data class TabItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen() {
    val navController = rememberNavController()
    
    val tabItems = listOf(
        TabItem(Screen.Timer, Icons.Default.Home, "Main"),
        TabItem(Screen.History, Icons.Default.List, "History"),
        TabItem(Screen.Profile, Icons.Default.Person, "Profile")
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                tabItems.forEach { tabItem ->
                    NavigationBarItem(
                        icon = { Icon(tabItem.icon, contentDescription = tabItem.label) },
                        label = { Text(tabItem.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == tabItem.screen.route } == true,
                        onClick = {
                            navController.navigate(tabItem.screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        TabNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}