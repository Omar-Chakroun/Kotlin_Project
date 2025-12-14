package com.omar.smartshop.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omar.smartshop.domain.repository.ProductRepository
import com.omar.smartshop.ui.dashboard.DashboardViewModel
import com.omar.smartshop.ui.products.ProductDetailViewModel
import com.omar.smartshop.ui.products.ProductListViewModel

/**
 * A factory for creating ViewModels with dependencies.
 */
class ViewModelFactory(
    private val productRepository: ProductRepository,
    private val productId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProductListViewModel::class.java) -> {
                ProductListViewModel(productRepository) as T
            }
            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> {
                ProductDetailViewModel(productId, productRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(productRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
