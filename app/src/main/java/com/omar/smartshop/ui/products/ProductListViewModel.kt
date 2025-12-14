package com.omar.smartshop.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Represents the state of the product list screen.
 */
sealed interface ProductListState {
    object Loading : ProductListState
    data class Success(val products: List<Product>) : ProductListState
    data class Error(val message: String) : ProductListState
}

/**
 * ViewModel for the Product List screen.
 */
class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val uiState: StateFlow<ProductListState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .onStart { _uiState.value = ProductListState.Loading }
                .catch { exception ->
                    _uiState.value = ProductListState.Error(exception.message ?: "An unknown error occurred")
                }
                .collect { products ->
                    _uiState.value = ProductListState.Success(products)
                }
        }
    }
}
