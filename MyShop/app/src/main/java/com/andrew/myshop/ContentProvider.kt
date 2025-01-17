package com.andrew.myshop

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.room.Room
import androidx.room.Room.inMemoryDatabaseBuilder
import java.security.AccessController.getContext


class ContentProvider : ContentProvider() {

//    var database: ProductDatabase = Room.databaseBuilder(this.context!!, ProductDatabase::class.java, "product.db").build()
lateinit var database: ProductDatabase;
    lateinit var productDao: ProductDao;

    companion object {
        const val authority = "com.andrew.myshop.provider"
        const val contentPath = "products"
        val CONTENT_URI: Uri = Uri.parse("content://$authority/$contentPath")
        const val CODE_CONTACTS = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(authority, contentPath, CODE_CONTACTS)
        }

//        val database: ProductDatabase = Room.inMemoryDatabaseBuilder(, ProductDatabase::class.java, "product.db").allowMainThreadQueries().build()

    }

    private val contacts = listOf(
        Pair("John Doe", "123456789"),
        Pair("Jane Smith", "987654321"),
        Pair("Alice Johnson", "555444333")
    )

    override fun onCreate(): Boolean {
        // Creates a new database object
        database = Room.databaseBuilder(context!!, ProductDatabase::class.java, "product.db").build()

        // Gets a Data Access Object to perform the database operations
        productDao = database.productDao()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

//        if (uriMatcher.match(uri) != CODE_CONTACTS) {
//            throw IllegalArgumentException("Unknown URI: $uri")
//        }

//        val cursor = MatrixCursor(arrayOf("Name", "Phone"))
//        contacts.forEach { contact ->
//            cursor.addRow(arrayOf(contact.first, contact.second))
//        }
//        var prodDAO = database.productDao()
        var cursor = productDao.selectAll()
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Insert not supported")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Update not supported")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Delete not supported")
    }

    override fun getType(uri: Uri): String? = null
}