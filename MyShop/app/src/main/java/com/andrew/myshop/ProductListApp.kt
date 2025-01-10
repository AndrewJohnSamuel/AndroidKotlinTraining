package com.andrew.myshop

import android.app.Application

class ProductListApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}