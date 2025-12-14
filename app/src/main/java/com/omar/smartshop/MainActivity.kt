package com.omar.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.omar.smartshop.navigation.AppNavHost
import com.omar.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as SmartShopApplication
        setContent {
            SmartShopTheme {
                AppNavHost(application = app)
            }
        }
    }
}
