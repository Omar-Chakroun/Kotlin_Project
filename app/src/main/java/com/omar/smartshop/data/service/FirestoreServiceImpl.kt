package com.omar.smartshop.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.domain.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * The concrete implementation of the FirestoreService for interacting with products.
 */
class FirestoreServiceImpl : FirestoreService {

    // Get a reference to the 'products' collection in Firestore
    private val productsCollection = FirebaseFirestore.getInstance().collection("products")

    override val products: Flow<List<Product>> = productsCollection.snapshots().map { snapshot ->
        snapshot.toObjects<Product>()
    }

    override suspend fun saveProduct(product: Product) {
        // Use the product's ID as the document ID in Firestore
        productsCollection.document(product.id).set(product).await()
    }

    override suspend fun deleteProduct(productId: String) {
        productsCollection.document(productId).delete().await()
    }
}
