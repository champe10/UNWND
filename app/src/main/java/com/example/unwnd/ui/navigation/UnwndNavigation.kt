package com.example.unwnd.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.unwnd.data.local.database.AppDatabase
import com.example.unwnd.data.local.datastore.UserPreferences
import com.example.unwnd.data.repository.PlaceRepository
import com.example.unwnd.ui.components.NavigationItem
import com.example.unwnd.ui.components.UnwndBottomNavigation
import com.example.unwnd.ui.screens.DetailScreen
import com.example.unwnd.ui.screens.ExploreScreen
import com.example.unwnd.ui.screens.FavoritesScreen
import com.example.unwnd.ui.screens.HomeScreen
import com.example.unwnd.ui.screens.ProfileScreen
import com.example.unwnd.ui.viewmodel.UnwndViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{placeId}") {
        fun createRoute(placeId: String) = "detail/$placeId"
    }
    object Explore : Screen("explore")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}

@Composable
fun UnwndNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Manual Dependency Injection
    val database = remember { AppDatabase.getDatabase(context) }
    val userPrefs = remember { UserPreferences(context) }
    val repository = remember { PlaceRepository(database.placeDao()) }
    
    val viewModel: UnwndViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UnwndViewModel(repository, userPrefs) as T
            }
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        NavigationItem(Screen.Home.route, Icons.Default.Home, "Home"),
        NavigationItem(Screen.Explore.route, Icons.Default.Search, "Explore"),
        NavigationItem(Screen.Favorites.route, Icons.Default.Favorite, "Saved"),
        NavigationItem(Screen.Profile.route, Icons.Default.Person, "Profile")
    )

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                UnwndBottomNavigation(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onPlaceClick = { placeId ->
                        navController.navigate(Screen.Detail.createRoute(placeId))
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("placeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getString("placeId") ?: ""
                DetailScreen(
                    placeId = placeId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToExplore = {
                        navController.navigate(Screen.Explore.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Explore.route) { 
                ExploreScreen(viewModel = viewModel)
            }
            composable(Screen.Favorites.route) { 
                FavoritesScreen(
                    viewModel = viewModel,
                    onPlaceClick = { placeId ->
                        navController.navigate(Screen.Detail.createRoute(placeId))
                    }
                )
            }
            composable(Screen.Profile.route) { 
                ProfileScreen()
            }
        }
    }
}
