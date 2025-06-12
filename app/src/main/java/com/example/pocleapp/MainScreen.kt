package com.example.pocleapp

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.pocleapp.navigation.Screen
import com.example.pocleapp.ui.screens.*

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Dashboard,
        Screen.Tracking,
        Screen.History,
        Screen.Profile,
        Screen.Articles
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = items)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(navController) }
            composable(Screen.Tracking.route) { MapScreen(context) }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Articles.route) { ArticlesScreen() }
        }
    }
}

@Composable
fun ArticlesScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ProfileScreen() {
    TODO("Not yet implemented")
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<Screen>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = screen.title)
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
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
