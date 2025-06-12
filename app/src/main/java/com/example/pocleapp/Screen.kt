package com.example.pocleapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class Screen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Dashboard : Screen("dashboard", "Beranda", Icons.Default.Home)
    object Tracking : Screen("tracking", "Mulai Jalan", Icons.Default.DirectionsRun)
    object History : Screen("history", "Riwayat", Icons.Default.History)
    object Profile : Screen("profile", "Profil", Icons.Default.Person)
    object Articles : Screen("articles", "Artikel", Icons.Default.MenuBook)
}
