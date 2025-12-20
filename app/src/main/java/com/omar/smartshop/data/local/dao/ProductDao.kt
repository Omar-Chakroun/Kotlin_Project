package com.omar.smartshop.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.omar.smartshop.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the products table.
 */
@Dao
interface ProductDao {

    /**
     * Inserts or updates a product in the database.
     * If a product with the same ID already exists, it will be replaced.
     *
     * @param product The product to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(product: ProductEntity)

    /**
     * Deletes a product from the database.
     *
     * @param product The product to be deleted.
     */
    @Delete
    suspend fun delete(product: ProductEntity)

    /**
     * Retrieves a single product from the database by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return A Flow emitting the product, or null if not found.
     */
    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity?>

    /**
     * Retrieves all products from the database, ordered by name.
     *
     * @return A Flow emitting the list of all products.
     */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>
}
