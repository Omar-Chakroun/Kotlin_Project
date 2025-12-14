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
 * The concrete implementation of the [ProductRepository].
 * This class is the single source of truth for all product data in the application.
 * It elegantly orchestrates data flow between the local database (Room) and the remote datastore (Firestore),
 * providing a clean, reactive API to the ViewModels.
 *
 * @param dao The Data Access Object for local product data.
 * @param firestoreService The service for interacting with the remote Firestore database.
 */
class ProductRepositoryImpl(
    private val dao: ProductDao,
    private val firestoreService: FirestoreService
) : ProductRepository {

    // A dedicated CoroutineScope for repository operations to run off the main thread.
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        // This is the core of our real-time synchronization.
        // We launch a coroutine that listens for any changes in the remote Firestore collection.
        repositoryScope.launch {
            firestoreService.products.collect { remoteProducts ->
                // When remote data changes, we update our local Room database.
                // The @Upsert annotation handles both inserts and updates seamlessly.
                // This makes our local database a real-time mirror of the remote one.
                remoteProducts.forEach { product ->
                    dao.upsert(product.toEntity())
                }
            }
        }
    }

    /**
     * The UI layer gets its data from Room. Since Room's queries return a Flow,
     * and our `init` block keeps Room updated from Firestore, the UI will automatically
     * update in real-time whenever the remote data changes.
     */
    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return dao.getProductById(id).map { it?.toProduct() }
    }

    /**
     * When saving a product, we follow a "remote-first" strategy.
     * 1. Save to Firestore.
     * 2. Save to Room.
     * This ensures the change is persisted remotely. The local save provides an instant UI update
     * without waiting for the Firestore listener to fire.
     */
    override suspend fun upsertProduct(product: Product) {
        firestoreService.saveProduct(product)
        dao.upsert(product.toEntity())
    }

    /**
     * Deletion also follows a "remote-first" strategy for consistency.
     */
    override suspend fun deleteProduct(product: Product) {
        firestoreService.deleteProduct(product.id)
        dao.delete(product.toEntity())
    }

    override fun getTotalProductCount(): Flow<Int> {
        // Statistics are calculated directly from the local database for efficiency.
        return dao.getTotalProductCount()
    }

    override fun getTotalStockValue(): Flow<Double> {
        return dao.getTotalStockValue()
    }
}

/**
 * Extension function to map a [ProductEntity] (local database model) to a [Product] (domain model).
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
 * Extension function to map a [Product] (domain model) to a [ProductEntity] (local database model).
 */
private fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}
