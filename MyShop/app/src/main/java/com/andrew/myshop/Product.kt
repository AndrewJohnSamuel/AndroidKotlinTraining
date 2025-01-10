package com.andrew.myshop

import android.content.Context
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "quantity")
    var quantity: Int,
    @ColumnInfo(name = "price")
    var price: Float,
    var isEditing: Boolean = false
)


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


