package com.omar.smartshop.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Represents the UI state for the product detail screen.
 */
sealed interface ProductDetailState {
    object Loading : ProductDetailState
    data class Success(val product: Product?) : ProductDetailState // product is null for a new product
    data class Error(val message: String) : ProductDetailState
    object Finished : ProductDetailState // To signal that the operation is done and we should navigate up
}

class ProductDetailViewModel(
    private val productId: String?,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    init {
        if (productId != null) {
            loadProduct(productId)
        } else {
            // This is a new product, so we start with an empty success state
            _uiState.value = ProductDetailState.Success(null)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            productRepository.getProductById(id).collect { product ->
                _uiState.value = ProductDetailState.Success(product)
            }
        }
    }

    fun saveProduct(name: String, priceStr: String, quantityStr: String) {
        val price = priceStr.toDoubleOrNull()
        val quantity = quantityStr.toIntOrNull()

        if (name.isBlank() || price == null || price <= 0 || quantity == null || quantity < 0) {
            // In a real app, you might want a more specific error message per field
            _uiState.value = ProductDetailState.Error("Invalid input. Please check all fields.")
            // It's a good practice to reset the state back to Success after an error
            viewModelScope.launch {
                 if (productId != null) loadProduct(productId) else _uiState.value = ProductDetailState.Success(null)
            }
            return
        }

        viewModelScope.launch {
            val productToSave = Product(
                id = productId ?: UUID.randomUUID().toString(), // Use existing ID or create a new one
                name = name,
                price = price,
                quantity = quantity
            )
            productRepository.upsertProduct(productToSave)
            _uiState.value = ProductDetailState.Finished
        }
    }

    fun deleteProduct() {
        // Only allow deletion if it's an existing product
        if (productId != null) {
            viewModelScope.launch {
                // We need to fetch the current product to delete it
                val currentState = _uiState.value
                if (currentState is ProductDetailState.Success && currentState.product != null) {
                    productRepository.deleteProduct(currentState.product)
                    _uiState.value = ProductDetailState.Finished
                } else {
                    _uiState.value = ProductDetailState.Error("Could not delete product. Product not found.")
                }
            }
        }
    }
}
