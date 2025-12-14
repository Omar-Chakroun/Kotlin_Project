package com.omar.smartshop.domain.repository

import com.omar.smartshop.data.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * The repository interface for product data.
 * This defines the contract for how ViewModels interact with product data,
 * abstracting the data sources.
 */
interface ProductRepository {

    fun getAllProducts(): Flow<List<Product>>

    fun getProductById(id: String): Flow<Product?>

    suspend fun upsertProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    /**
     * Gets a real-time count of all products.
     */
    fun getTotalProductCount(): Flow<Int>

    /**
     * Gets the total value of all stock in real-time.
     */
    fun getTotalStockValue(): Flow<Double>
}
