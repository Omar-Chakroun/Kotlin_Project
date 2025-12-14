package com.omar.smartshop.domain.service

import com.omar.smartshop.data.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * An interface that defines the contract for interacting with the remote product database (Firestore).
 */
interface FirestoreService {

    /**
     * A flow that emits real-time updates from the products collection in Firestore.
     */
    val products: Flow<List<Product>>

    /**
     * Saves (adds or updates) a product in Firestore.
     *
     * @param product The product to be saved.
     */
    suspend fun saveProduct(product: Product)

    /**
     * Deletes a product from Firestore.
     *
     * @param productId The ID of the product to be deleted.
     */
    suspend fun deleteProduct(productId: String)
}
