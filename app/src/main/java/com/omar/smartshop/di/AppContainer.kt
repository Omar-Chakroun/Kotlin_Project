package com.omar.smartshop.di

import android.content.Context
import com.omar.smartshop.data.local.SmartShopDatabase
import com.omar.smartshop.data.repository.ProductRepositoryImpl
import com.omar.smartshop.data.service.FirestoreServiceImpl
import com.omar.smartshop.domain.repository.ProductRepository
import com.omar.smartshop.domain.service.FirestoreService

/**
 * A simple container for managing dependencies manually.
 * In a larger app, this would be replaced by a library like Hilt.
 */
class AppContainer(context: Context) {

    // The remote data source
    private val firestoreService: FirestoreService by lazy {
        FirestoreServiceImpl()
    }

    // The local data source
    private val database by lazy { SmartShopDatabase.getInstance(context) }
    private val productDao by lazy { database.productDao() }

    // The Product Repository, which orchestrates data between sources
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(productDao, firestoreService)
    }
}
