package com.omar.smartshop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a product in the local Room database.
 *
 * @property id The unique identifier for the product.
 * @property name The name of the product.
 * @property price The price of the product.
 * @property quantity The stock quantity of the product.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)
