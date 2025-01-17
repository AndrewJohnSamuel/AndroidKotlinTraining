package com.andrew.mycontentproviderresolver


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun contactPicker(
    context: Context,
    contactName: String,
    contactNumber: String,
    modifier: Modifier
): List<Pair<String, String>> {
    val contactSingle = mutableListOf<Pair<String, String>>()
    val activity = LocalContext.current as Activity
    // Mutable state for storing the list of contacts
    var contacts by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    contacts = fetchContactsStartingWithAlphabet("", context)
    // Using Scaffold to position content below the TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Read Contact",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.Blue
                )
            )
        },
        content = { paddingValues ->
            // Column will start below the TopAppBar automatically
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Add padding from Scaffold
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = "Contact in Android",
                    color = Color.Green,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp) // Added padding for better visibility
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (contactName.isNotEmpty()) {
                    Text(
                        text = contactName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                if (contactNumber.isNotEmpty()) {
                    Text(
                        text = contactNumber,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                var singleContact: Boolean = false
                if (hasContactPermission(context)) {
                    requestContactPermission(context, activity)
                }
                Button(
                    onClick = {
                        singleContact = true
                        //if (hasContactPermission(context)) {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        startActivityForResult(activity, intent, 1, null)

                        //} else {
                        //    requestContactPermission(context, activity)
                        //}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = "Pick Contact")
                }
                var textValue by remember { mutableStateOf("") }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        modifier = Modifier
                            .weight(1f), // Takes up the remaining space in the row
                        label = { Text("Enter starting Alphabet") }
                    )

                    Button(
                        onClick = {
                            //ContactList(textValue, context)
                            singleContact = false
                            contacts = fetchContactsStartingWithAlphabet(textValue, context)
                        },
                        modifier = Modifier
                            .height(56.dp) // Match TextField height
                    ) {
                        Text("Submit")
                    }
                }

                if (contactName.isNotEmpty() && contactNumber.isNotEmpty() && singleContact) {
                    contactSingle.add(Pair(contactName, contactNumber))
                    contacts = contactSingle
                }

                // Display the contacts in a list
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    if (contacts.isEmpty()) {
                        item {
                            Text(
                                text = "No contacts found.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    } else {
                        items(contacts) { contact ->
                            ContactItem(contactName = contact.first, contactNumber = contact.second)
                        }
                    }
                }
            }
        }
    )
    return contacts
}

// Composable to display a single contact item
@Composable
fun ContactItem(contactName: String, contactNumber: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = contactName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contactNumber,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
    }
}

//@Composable
//fun ContactList(alphabet: String, context: Context) {
//
//    LaunchedEffect(Unit) {
//        contacts = fetchContactsStartingWithAlphabet(alphabet, context)
//    }
//}

// Function to fetch contacts starting with "A"
fun fetchContactsStartingWithAlphabet(alphabet: String, context: Context): List<Pair<String, String>> {
    val contacts = mutableListOf<Pair<String, String>>()
    val contentResolver = context.contentResolver

    // Define projection and selection for the query
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
    //val selectionArgs = arrayOf("A%")
    val selectionArgs = arrayOf("$alphabet%")

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC" // Order by name
    )

    cursor?.use {
        while (it.moveToNext()) {
            val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contacts.add(Pair(name, number))
        }
    }
    return contacts
}