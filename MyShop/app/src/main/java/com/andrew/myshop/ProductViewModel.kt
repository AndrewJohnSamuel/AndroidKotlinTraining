package com.andrew.myshop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository = Graph.productRepository
): ViewModel() {
    var nameState by mutableStateOf("")
    var quantityStage by mutableStateOf("")
    var priceStage by mutableStateOf("")

    fun onNameChanged(name: String) {
        nameState = name
    }

    fun onQuantityChanged(quantity: String) {
        quantityStage = quantity
    }

    fun onPriceChanged(price: String) {
        priceStage = price
    }

    lateinit var getAllProducts: Flow<List<Product>>

    init {
        viewModelScope.launch {
            getAllProducts = productRepository.getAllProducts()
        }
    }

    fun addProduct(prod: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.addProduct(prod = prod)
        }
    }

    fun updateProduct(prod: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateProduct(prod = prod)
        }
    }

    fun getProductById(id: Int): Flow<Product> {
        return productRepository.getProductById(id)
    }

    fun deleteProduct(prod: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(prod = prod)
        }
    }
}