package com.omar.smartshop.data.repository

import com.omar.smartshop.data.local.dao.ProductDao
import com.omar.smartshop.data.local.entity.ProductEntity
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.domain.repository.ProductRepository
import com.omar.smartshop.domain.service.FirestoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * The concrete implementation of the ProductRepository.
 * This class is the single source of truth for all product data.
 * It orchestrates data between the local database (Room) and the remote datastore (Firestore).
 *
 * @param dao The Data Access Object for products.
 * @param firestoreService The service for interacting with Firestore.
 */
class ProductRepositoryImpl(
    private val dao: ProductDao,
    private val firestoreService: FirestoreService
) : ProductRepository {

    // A dedicated scope for repository operations
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        // Start listening for real-time updates from Firestore
        // and update the local database accordingly.
        repositoryScope.launch {
            firestoreService.products.collect { remoteProducts ->
                // In a more complex app, you'd perform a diff
                // and only update what's changed. For now, we'll
                // just insert all remote products, which will
                // overwrite local ones due to the @Upsert strategy.
                remoteProducts.forEach { product ->
                    dao.upsert(product.toEntity())
                }
            }
        }
    }

    override fun getAllProducts(): Flow<List<Product>> {
        // The UI will now get real-time updates from Room, which are sourced from Firestore.
        return dao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return dao.getProductById(id).map { it?.toProduct() }
    }

    override suspend fun upsertProduct(product: Product) {
        // First, save to the remote source (Firestore)
        firestoreService.saveProduct(product)
        // Then, save to the local cache (Room). The remote listener will also pick this up,
        // but upserting it here makes the UI update instantly.
        dao.upsert(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        // First, delete from the remote source
        firestoreService.deleteProduct(product.id)
        // Then, delete from the local cache
        dao.delete(product.toEntity())
    }
}

/**
 * Extension function to map a [ProductEntity] to a [Product] domain model.
 */
private fun ProductEntity.toProduct(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}

/**
 * Extension function to map a [Product] domain model to a [ProductEntity].
 */
private fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}
