package com.omar.smartshop

import android.app.Application
import com.omar.smartshop.di.AppContainer

/**
 * The custom Application class for the SmartShop app.
 * This class holds the singleton instance of the AppContainer.
 */
class SmartShopApplication : Application() {

    // The application-wide dependency container.
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
