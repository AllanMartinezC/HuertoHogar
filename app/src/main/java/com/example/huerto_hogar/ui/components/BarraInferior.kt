package com.example.huerto_hogar.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val titulo: String, val icon: ImageVector, val screenRoute: String)

@Composable
fun BottomBar(navController: NavController, items: List<BottomNavItem>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.screenRoute
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.titulo) },
                label = { Text(item.titulo) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.screenRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
