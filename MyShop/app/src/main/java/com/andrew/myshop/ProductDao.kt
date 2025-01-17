package com.andrew.myshop

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDao {
    @Query("SELECT * FROM `Product`")
    abstract fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM `Product`")
    abstract fun selectAll(): Cursor

    @Query("SELECT * FROM `Product` WHERE id=:id")
    abstract fun getProductById(id: Int): Flow<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addProduct(prodEntity: Product)

    @Update
    abstract suspend fun editProduct(prodEntity: Product)

    @Delete
    abstract suspend fun deleteProduct(prodEntity: Product)
}