package com.andrew.myshop

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrew.myshop.ui.theme.MyShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prodViewModel: ProductViewModel by viewModels()
//            Graph.provide(this)
            MyShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(20.dp)) { innerPadding ->
                    Greeting(
                        prodViewModel = prodViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(prodViewModel: ProductViewModel, modifier: Modifier = Modifier) {
    var context = LocalContext.current
    var sProducts by remember { mutableStateOf(listOf<Product>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(186, 229, 247)).padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Products", fontSize = 25.sp)
            IconButton(
                modifier = Modifier.offset(x = 0.dp, y = 0.dp).size(25.dp),
                onClick = {
                    showAddDialog = true;
                }
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            sProducts = prodViewModel.getAllProducts.collectAsState(initial = listOf()).value
//            ProductListApp(sProducts)
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Add a single item
//                item {
//                    Text(text = "First item")
//                }

                // Add 5 items
                items(sProducts) { item ->

                    ProductListApp(context, item, {
                        showEditDialog = true
                        item.isEditing = true

                }, {
                    prodViewModel.deleteProduct(item)
                })
                }

                // Add another single item
//                item {
//                    Text(text = "Last item")
//                }
            }
        }
    }

    if(showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        showAddDialog = false;
                    }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        if(productName.isNotBlank() && productQuantity.isNotBlank() && productPrice.isNotBlank()) {
                            var prod = Product(
                                id = sProducts.size+1,
                                name = productName,
                                quantity = productQuantity.toInt(),
                                price = productPrice.toFloat()

                            )
//                            sProducts = sProducts + prod
                            prodViewModel.addProduct(prod)
                            showAddDialog = false
                            productName = ""
                            productQuantity = ""
                            productPrice = ""
                        }
                        else {
                            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
                        }
                    }) {
                        Text("Add")
                    }
                }
            },
            title = { Text("Add new Product") },
            text = {
                Column {
                    OutlinedTextField(value = productName, onValueChange = {
                        productName = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    OutlinedTextField(value = productQuantity, onValueChange = {
                        productQuantity = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    OutlinedTextField(value = productPrice, onValueChange = {
                        productPrice = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                }
            }
        )
//        AlertDialog(onDismissRequest = {
//                showAddDialog = false
//            })
//        {
//            Text("Add new Product")
//        }
    }

    if(showEditDialog) {
        var prod = sProducts.find{ it.isEditing == true }
        if (prod != null) {
            ProductEditor(
                context = context,
                item = prod,
                onEditComplete = { name: String, quantity: Int, price: Float ->
                    sProducts = sProducts.map{ it.copy(isEditing = false )}
                    var editedItem = sProducts.find{ it.id == prod.id }
                    editedItem?.let {
                        it.name = name
                        it.quantity = quantity
                        it.price = price
                    }
                    if (editedItem != null) {
                        prodViewModel.updateProduct(
                            prod = editedItem
                        )
                    }
                    showEditDialog = false
                },
                onDismiss = {
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun ProductEditor(context: Context, item: Product, onEditComplete: (name: String, quantity: Int, price: Float) -> Unit, onDismiss: () -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var editedPrice by remember { mutableStateOf(item.price.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing.toString()) }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    OutlinedTextField(value = editedName, onValueChange = {
                        editedName = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    OutlinedTextField(value = editedQuantity, onValueChange = {
                        editedQuantity = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    OutlinedTextField(value = editedPrice, onValueChange = {
                        editedPrice = it
                    }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    Button(onClick = {
                        isEditing = "true"
                        onEditComplete(
                            editedName,
                            editedQuantity.toIntOrNull() ?: 1,
                            editedPrice.toFloatOrNull() ?: 1f
                        )
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyShopTheme {
//        Greeting("Android")
//    }
//}