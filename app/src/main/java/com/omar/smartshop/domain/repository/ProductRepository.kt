package com.omar.smartshop.domain.repository

import com.omar.smartshop.data.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * The repository interface for product data.
 * This defines the contract for how ViewModels interact with product data,
 * abstracting the data sources.
 */
interface ProductRepository {

    /**
     * Retrieves all products as a Flow.
     * @return A Flow emitting a list of all products.
     */
    fun getAllProducts(): Flow<List<Product>>

    /**
     * Retrieves a single product by its ID.
     * @param id The ID of the product to retrieve.
     * @return A Flow emitting the product, or null if not found.
     */
    fun getProductById(id: String): Flow<Product?>

    /**
     * Inserts or updates a product.
     * @param product The product to be saved.
     */
    suspend fun upsertProduct(product: Product)

    /**
     * Deletes a product.
     * @param product The product to be deleted.
     */
    suspend fun deleteProduct(product: Product)
}
