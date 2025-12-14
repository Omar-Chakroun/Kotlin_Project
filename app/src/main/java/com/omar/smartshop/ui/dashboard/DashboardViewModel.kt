package com.omar.smartshop.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.smartshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the Dashboard screen.
 */
class DashboardViewModel(productRepository: ProductRepository) : ViewModel() {

    /**
     * A state flow that holds the total number of products, updated in real-time.
     */
    val totalProductCount: StateFlow<Int> = productRepository.getTotalProductCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * A state flow that holds the total stock value, updated in real-time.
     */
    val totalStockValue: StateFlow<Double> = productRepository.getTotalStockValue()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
}
