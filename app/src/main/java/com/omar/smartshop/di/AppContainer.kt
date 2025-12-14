package com.omar.smartshop.di

import android.content.Context
import com.omar.smartshop.data.local.SmartShopDatabase
import com.omar.smartshop.data.repository.ProductRepositoryImpl
import com.omar.smartshop.domain.repository.ProductRepository

/**
 * A simple container for managing dependencies manually.
 * In a larger app, this would be replaced by a library like Hilt.
 */
class AppContainer(context: Context) {

    // The Room database instance
    private val database by lazy { SmartShopDatabase.getInstance(context) }

    // The Product DAO instance from the database
    private val productDao by lazy { database.productDao() }

    // The Product Repository instance
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(productDao)
    }
}
