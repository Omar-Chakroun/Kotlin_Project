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

class ProductRepositoryImpl(
    private val dao: ProductDao,
    private val firestoreService: FirestoreService
) : ProductRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        repositoryScope.launch {
            firestoreService.products.collect { remoteProducts ->
                remoteProducts.forEach { product ->
                    dao.upsert(product.toEntity())
                }
            }
        }
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return dao.getProductById(id).map { it?.toProduct() }
    }

    override suspend fun upsertProduct(product: Product) {
        firestoreService.saveProduct(product)
        dao.upsert(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        firestoreService.deleteProduct(product.id)
        dao.delete(product.toEntity())
    }

    override fun getTotalProductCount(): Flow<Int> {
        return dao.getTotalProductCount()
    }

    override fun getTotalStockValue(): Flow<Double> {
        return dao.getTotalStockValue()
    }
}

private fun ProductEntity.toProduct(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}

private fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}
