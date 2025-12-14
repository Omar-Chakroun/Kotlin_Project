package com.omar.smartshop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omar.smartshop.SmartShopApplication
import com.omar.smartshop.ui.products.ProductDetailScreen
import com.omar.smartshop.ui.products.ProductListScreen

/**
 * The main navigation host for the application.
 */
@Composable
fun AppNavHost(application: SmartShopApplication) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "productList") {
        composable("productList") {
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
