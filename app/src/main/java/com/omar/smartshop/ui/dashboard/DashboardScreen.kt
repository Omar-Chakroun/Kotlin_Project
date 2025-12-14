package com.omar.smartshop.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.smartshop.SmartShopApplication
import com.omar.smartshop.di.ViewModelFactory
import java.text.NumberFormat

@Composable
fun DashboardScreen(application: SmartShopApplication) {
    val viewModel: DashboardViewModel = viewModel(
        factory = ViewModelFactory(application.appContainer.productRepository)
    )
    val totalProductCount by viewModel.totalProductCount.collectAsState()
    val totalStockValue by viewModel.totalStockValue.collectAsState()

    DashboardScreenContent(
        totalProductCount = totalProductCount,
        totalStockValue = totalStockValue
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenContent(totalProductCount: Int, totalStockValue: Double) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Dashboard") }) }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(16.dp)
        ) {
            StatisticCard(
                label = "Total Products",
                value = totalProductCount.toString()
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatisticCard(
                label = "Total Stock Value",
                value = formatCurrency(totalStockValue)
            )
        }
    }
}

@Composable
private fun StatisticCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance().format(value)
}
