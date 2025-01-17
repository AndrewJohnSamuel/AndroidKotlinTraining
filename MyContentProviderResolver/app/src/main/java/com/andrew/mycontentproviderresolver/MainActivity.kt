package com.andrew.mycontentproviderresolver

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.andrew.mycontentproviderresolver.ui.theme.MyContentProviderResolverTheme

class MainActivity : ComponentActivity() {
    // Mutable states for contact name and number
    var pickedContactName by mutableStateOf("")
    var pickedContactNumber by mutableStateOf("")

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyContentProviderResolverTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    if(!hasContactPermission(LocalContext.current)) {
                        requestContactPermission(LocalContext.current, this)
                    }
                    var contacts = contactPicker(
                        context = LocalContext.current, pickedContactName, pickedContactNumber, modifier = Modifier
                    )
                    contacts.forEach { contact ->
                        val (name, number) = contact
                        Log.d(TAG, "Inside Main Contact Name: $name, Contact Number: $number")
                    }
                    //ContactList(context = LocalContext.current, contacts)
                }
            }
        }
    }

@Deprecated("This method has been deprecated in favor of the Activity Result API")
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    Log.d(TAG, "Inside OnActivityResult")
    // Check if the result is OK
    if (resultCode != Activity.RESULT_OK || data == null) return

    if (requestCode == 1) {
        // Get contact data
        Log.d(TAG, "Inside Request")
        val contactData: Uri? = data.data
        if (contactData != null) {  // Ensure `contactData` is not null
            val cursor: Cursor? = contentResolver.query(contactData, null, null, null, null)
            cursor?.use { cursor ->  // Use `cursor` instead of `it` explicitly
                if (cursor.moveToFirst()) {
                    val number: String = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    val name: String = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )
                    Log.d(TAG, "Inside Contact Name: $name, Contact Number: $number")
                    pickedContactName = name
                    pickedContactNumber = number
                    //contacts.add(Pair(name, number))
                }
            }
        }
    }
}

}

    fun hasContactPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED;
}

fun requestContactPermission(context: Context, activity: Activity) {
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}

//Functions to read list of contacts

@Composable
fun ContactListPicker(
    context: Context
) {
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                if (hasContactPermission(context)) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    //ContactList(context = context)
                } else {
                    requestContactPermission(context, activity)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Pick Contact")
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
    MyContentProviderResolverTheme {
        Greeting("Android")
    }
}