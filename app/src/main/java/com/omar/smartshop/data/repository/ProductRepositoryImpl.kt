package com.omar.smartshop.data.repository

import com.omar.smartshop.data.local.dao.ProductDao
import com.omar.smartshop.data.local.entity.ProductEntity
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * The concrete implementation of the ProductRepository.
 * This class is the single source of truth for all product data.
 *
 * @param dao The Data Access Object for products.
 */
class ProductRepositoryImpl(private val dao: ProductDao) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return dao.getProductById(id).map { it?.toProduct() }
    }

    override suspend fun upsertProduct(product: Product) {
        dao.upsert(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
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
