package com.omar.smartshop.ui.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx-compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.smartshop.R
import com.omar.smartshop.SmartShopApplication
import com.omar.smartshop.data.model.Product
import com.omar.smartshop.di.ViewModelFactory

@Composable
fun ProductListScreen(application: SmartShopApplication) {
    val viewModel: ProductListViewModel = viewModel(
        factory = ViewModelFactory(application.appContainer.productRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    ProductListScreenContent(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListScreenContent(uiState: ProductListState) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to Add Product */ }) {
                Icon(painter = painterResource(id = android.R.drawable.ic_input_add), contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is ProductListState.Loading -> {
                    CircularProgressIndicator()
                }
                is ProductListState.Success -> {
                    if (uiState.products.isEmpty()) {
                        Text("No products found. Add one!")
                    } else {
                        ProductList(products = uiState.products)
                    }
                }
                is ProductListState.Error -> {
                    Text(uiState.message)
                }
            }
        }
    }
}

@Composable
private fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductListItem(product = product)
        }
    }
}

@Composable
private fun ProductListItem(product: Product) {
    ListItem(
        headlineContent = { Text(product.name) },
        supportingContent = {
            Column {
                Text("Price: $${product.price}")
                Text("Quantity: ${product.quantity}")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
