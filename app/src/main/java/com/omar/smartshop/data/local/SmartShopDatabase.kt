package com.omar.smartshop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omar.smartshop.data.local.dao.ProductDao
import com.omar.smartshop.data.local.entity.ProductEntity

/**
 * The Room database for the SmartShop application.
 *
 * This abstract class defines the database configuration and serves as the main access point
 * to the persisted data. It lists the entities contained within the database and the DAOs that
 * provide access to them.
 *
 * @see ProductEntity
 * @see ProductDao
 */
@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false // Schema export is good for complex migrations, but not needed for this project.
)
abstract class SmartShopDatabase : RoomDatabase() {

    /**
     * Provides an instance of the [ProductDao] to interact with the products table.
     */
    abstract fun productDao(): ProductDao

    companion object {
        // The name of the database file.
        private const val DATABASE_NAME = "smartshop_database"

        // @Volatile ensures that the INSTANCE variable is always up-to-date and visible to all threads.
        @Volatile
        private var INSTANCE: SmartShopDatabase? = null

        /**
         * Returns a singleton instance of the [SmartShopDatabase].
         *
         * This method uses a synchronized block to prevent race conditions during the creation
         * of the database instance, ensuring that only one instance is created.
         *
         * @param context The application context.
         * @return The singleton [SmartShopDatabase] instance.
         */
        fun getInstance(context: Context): SmartShopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartShopDatabase::class.java,
                    DATABASE_NAME
                )
                // In a real app, you would add a migration strategy here.
                // .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
