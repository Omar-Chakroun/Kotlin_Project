package com.omar.smartshop.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omar.smartshop.domain.repository.ProductRepository
import com.omar.smartshop.ui.products.ProductListViewModel

/**
 * A factory for creating ViewModels with dependencies.
 */
class ViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
