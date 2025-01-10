package com.andrew.myshop

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    suspend fun addProduct(prod: Product) {
        productDao.addProduct(prodEntity = prod)
    }

    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    fun getProductById(id: Int): Flow<Product> {
        return productDao.getProductById(id)
    }

    suspend fun updateProduct(prod: Product) {
        productDao.editProduct(prod)
    }

    suspend fun deleteProduct(prod: Product) {
        productDao.deleteProduct(prod)
    }
}