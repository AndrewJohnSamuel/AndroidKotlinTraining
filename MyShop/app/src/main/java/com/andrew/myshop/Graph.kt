package com.andrew.myshop

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: ProductDatabase

    val productRepository by lazy {
        ProductRepository(productDao = database.productDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, ProductDatabase::class.java, "product.db").build()
    }
}