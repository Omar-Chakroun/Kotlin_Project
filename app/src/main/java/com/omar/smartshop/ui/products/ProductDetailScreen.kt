package com.omar.smartshop.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.smartshop.SmartShopApplication
import com.omar.smartshop.di.ViewModelFactory

@Composable
fun ProductDetailScreen(
    application: SmartShopApplication,
    productId: String?,
    onNavigateUp: () -> Unit
) {
    val viewModel: ProductDetailViewModel = viewModel(
        factory = ViewModelFactory(application.appContainer.productRepository, productId)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is ProductDetailState.Finished) {
            onNavigateUp()
        }
    }

    ProductDetailScreenContent(
        uiState = uiState,
        onSaveClick = { name, price, quantity -> viewModel.saveProduct(name, price, quantity) },
        onDeleteClick = { viewModel.deleteProduct() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailScreenContent(
    uiState: ProductDetailState,
    onSaveClick: (String, String, String) -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Product Details") })
        }
    ) {
        when (uiState) {
            is ProductDetailState.Loading -> {
                CircularProgressIndicator()
            }
            is ProductDetailState.Success -> {
                ProductDetailForm(uiState.product, onSaveClick, onDeleteClick)
            }
            is ProductDetailState.Error -> {
                Text(uiState.message, color = MaterialTheme.colorScheme.error)
            }
            is ProductDetailState.Finished -> {
                // Handled in LaunchedEffect
            }
        }
    }
}

@Composable
fun ProductDetailForm(
    product: com.omar.smartshop.data.model.Product?,
    onSaveClick: (String, String, String) -> Unit,
    onDeleteClick: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var quantity by remember { mutableStateOf(product?.quantity?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onSaveClick(name, price, quantity) }) {
                Text("Save")
            }
            if (product != null) { // Only show delete for existing products
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
