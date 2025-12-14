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

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    /**
     * Gets a real-time count of all products in the database.
     *
     * @return A Flow emitting the total number of products.
     */
    @Query("SELECT COUNT(id) FROM products")
    fun getTotalProductCount(): Flow<Int>

    /**
     * Calculates the total value of all stock (sum of price * quantity for all products).
     *
     * @return A Flow emitting the total stock value.
     */
    @Query("SELECT SUM(price * quantity) FROM products")
    fun getTotalStockValue(): Flow<Double>
}
