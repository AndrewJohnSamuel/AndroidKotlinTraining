package com.andrew.mycontentreceiver

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrew.mycontentreceiver.ui.theme.MyContentReceiverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyContentReceiverTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var context = this.applicationContext
                    FetchContentProviders(
                        context = context,
                        fetchProducts = { fetchProductsFromCustomContentProvider(context) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private fun fetchProductsFromCustomContentProvider(context: Context): List<Product> {
    val contentResolver = context.contentResolver
    val productList = mutableListOf<Product>()

    val cursor = contentResolver.query(
        Uri.parse("content://com.andrew.myshop.provider/products"),
        null, null, null, null
    )

    if (cursor != null) {
        if (cursor.moveToFirst()) {
            do {
                val idColumnIndex = cursor.getColumnIndex("id")
                val nameColumnIndex = cursor.getColumnIndex("name")
                val quantityColumnIndex = cursor.getColumnIndex("quantity")
                val priceColumnIndex = cursor.getColumnIndex("price")
                var id = 0;

                if (idColumnIndex != -1) {
                    id = cursor.getString(idColumnIndex).toInt()
                }
                var name = ""
                if (nameColumnIndex != -1) {
                    name = cursor.getString(nameColumnIndex)
                }
                var quantity = 0
                if (quantityColumnIndex != -1) {
                    quantity = cursor.getString(quantityColumnIndex).toInt()
                }
                var price = 0.0f
                if (priceColumnIndex != -1) {
                    price = cursor.getString(priceColumnIndex).toFloat()
                }
                var product: Product = Product(id, name, quantity, price);
                productList.add(product)
                //val data = cursor.getString(cursor.getColumnIndex("name"))
                // do what ever you want here
            } while (cursor.moveToNext())
        }
        cursor.close()
//        val quoteColumnIndex = cursor.getColumnIndex("name")
//        if (quoteColumnIndex != -1) {
//            while (cursor.moveToNext()) {
//                val quote = cursor.getString(quoteColumnIndex)
//                productList.add(quote)
//            }
//            cursor.close()
//        } else {
//            println("Column 'quote' not found!")
//        }
    } else {
        println("Cursor is null or no data found!")
    }
    return productList
}

@Composable
fun FetchContentProviders(
    context: Context,
    fetchProducts: () -> List<Product>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //var productnames: List<String> by remember { mutableStateOf(emptyList()) }
        var products = fetchProducts();
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(products) { item ->
                ProductListApp(context, item, {
//                    showEditDialog = true
//                    item.isEditing = true

                }, {
//                    prodViewModel.deleteProduct(item)
                })

            }
        }
//        Row {
//            Button(
//                onClick = {
//                    quotesList = fetchQuotes()
//                    imagesList = emptyList()
//                }
//            ) {
//                Text("Fetch Quotes")
//            }
//            Spacer(Modifier.width(16.dp))
//            Button(
//                onClick = {
//                    imagesList = fetchImages()
//                    quotesList = emptyList()
//                }
//            ) {
//                Text("Fetch Images")
//            }
//        }
//        Spacer(Modifier.height(8.dp))
//        LazyColumn {
//            items(quotesList.size) { idx ->
//                Text(
//                    text = quotesList[idx],
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//
//        LazyColumn() {
//            items(imagesList) { uri ->
//                Image(
//                    painter = rememberAsyncImagePainter(uri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .heightIn(min = 150.dp)
//                        .padding(bottom = 8.dp)
//                        .fillMaxWidth(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
    }
}

@Composable
fun ProductListApp(context: Context, prod: Product, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth().border(border = BorderStroke(2.dp, Color(0xFF018786)), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(prod.name)
        Text("Quantity: ${prod.quantity}", textAlign = TextAlign.Start)
        Text("Price: ${prod.price}", textAlign = TextAlign.Start)
        Row(
        ) {
            IconButton(onClick = {

                onEditClick()
            }, modifier = Modifier.width(30.dp)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = {
                onDeleteClick()
            }, modifier = Modifier.width(30.dp)) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyContentReceiverTheme {
        Greeting("Android")
    }
}