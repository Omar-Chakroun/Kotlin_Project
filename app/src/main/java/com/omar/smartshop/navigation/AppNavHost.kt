package com.omar.smartshop.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omar.smartshop.SmartShopApplication
import com.omar.smartshop.ui.dashboard.DashboardScreen
import com.omar.smartshop.ui.products.ProductDetailScreen
import com.omar.smartshop.ui.products.ProductListScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object ProductList : Screen("productList", "Products", Icons.Default.List)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
}

val items = listOf(
    Screen.ProductList,
    Screen.Dashboard,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(application: SmartShopApplication) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ProductList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ProductList.route) {
                ProductListScreen(
                    application = application,
                    onProductClick = { productId ->
                        navController.navigate("productDetail/$productId")
                    },
                    onAddProductClick = {
                        navController.navigate("productDetail")
                    }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(application = application)
            }
            composable("productDetail/{productId?}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                ProductDetailScreen(
                    application = application,
                    productId = productId,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}
