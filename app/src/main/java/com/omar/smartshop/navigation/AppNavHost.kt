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

/**
 * Represents the primary screens in the bottom navigation bar.
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object ProductList : Screen("productList", "Products", Icons.Default.List)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
}

// List of screens to be displayed in the bottom navigation bar.
private val bottomNavItems = listOf(
    Screen.ProductList,
    Screen.Dashboard,
)

/**
 * The main navigation component for the application.
 *
 * This Composable sets up a [Scaffold] with a [NavigationBar] and a [NavHost]. It is responsible
 * for handling navigation between the main screens (Products and Dashboard) and the detail screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(application: SmartShopApplication) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item.
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item.
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // The NavHost contains the different screens of the app.
        NavHost(
            navController = navController,
            startDestination = Screen.ProductList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // The Product List screen, which is the start destination.
            composable(Screen.ProductList.route) {
                ProductListScreen(
                    application = application,
                    onProductClick = { productId ->
                        // Navigate to the detail screen with a specific product ID.
                        navController.navigate("productDetail/$productId")
                    },
                    onAddProductClick = {
                        // Navigate to the detail screen without a product ID (for creating a new product).
                        navController.navigate("productDetail")
                    }
                )
            }
            // The Dashboard screen.
            composable(Screen.Dashboard.route) {
                DashboardScreen(application = application)
            }
            // The Product Detail screen. The {productId} argument is optional.
            composable("productDetail/{productId?}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                ProductDetailScreen(
                    application = application,
                    productId = productId,
                    onNavigateUp = { navController.navigateUp() } // A callback to navigate back.
                )
            }
        }
    }
}
