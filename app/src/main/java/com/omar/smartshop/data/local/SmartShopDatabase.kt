package com.omar.smartshop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omar.smartshop.data.local.dao.ProductDao
import com.omar.smartshop.data.local.entity.ProductEntity

/**
 * The Room database for the application.
 */
@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartShopDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: SmartShopDatabase? = null

        fun getInstance(context: Context): SmartShopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartShopDatabase::class.java,
                    "smartshop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
