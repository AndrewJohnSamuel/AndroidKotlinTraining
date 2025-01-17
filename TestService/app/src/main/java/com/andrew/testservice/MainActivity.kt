package com.andrew.testservice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import com.andrew.testservice.ui.theme.TestServiceTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestServiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if(!isSmsPermissionGranted(this)) {
                        requestReadAndSendSmsPermission(this);
                    }
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var context = LocalContext.current
    var aeroplane_change: Aeroplane_Broadcast? = null
    var sms_receive: sms_broadcast? = null
    var custom_receive: CustomBroadcast? = null
Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
//    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
//        Text(
//            text = "Hello $name!",
//            modifier = modifier
//        )
//    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
//                ContextCompat.startForegroundService(context,
//                    Intent(
//                        context,
//                        MyForegroundService::class.java
//                    )
//                )
                context.startForegroundService(
                    Intent(
                        context,
                        MyForegroundService::class.java
                    )
                )
            }) {
                Text(text = "Start Foreground Service")
            }
            Button(onClick = {
               context.stopService(
                    Intent(
                        context,
                        MyForegroundService::class.java
                    )
                )
            }) {
                Text(text = "Stop Foreground Service")
            }
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                context.startService(
                    Intent(
                        context,
                        MyBackgroundService::class.java
                    )
                )
            }) {
                Text(text = "Start Background Service")
            }
            Button(onClick = {
                context.stopService(
                    Intent(
                        context,
                        MyBackgroundService::class.java
                    )
                )
            }) {
                Text(text = "Stop Background Service")
            }
        }
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE");
                aeroplane_change = Aeroplane_Broadcast()
                registerReceiver(
                    context,
                    aeroplane_change,
                    intentFilter,
                    ContextCompat.RECEIVER_EXPORTED
                )

            }) {
                Text(text = "Start Aeroplane mode Broadcast Service")
            }
            Button(onClick = {
                if (aeroplane_change != null) {
                    context.unregisterReceiver(aeroplane_change)
                }
            }) {
                Text(text = "Stop Aeroplane mode Broadcast Service")
            }
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
//                if(isSmsPermissionGranted(context)) {
                    val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    sms_receive = sms_broadcast()
                    registerReceiver(
                        context,
                        sms_receive,
                        intentFilter,
                        ContextCompat.RECEIVER_EXPORTED
                    )
//                }else {
//                    requestReadAndSendSmsPermission()
//                }

            }) {
                Text(text = "Start SMS Broadcast Service")
            }
            Button(onClick = {
                if (aeroplane_change != null) {
                    context.unregisterReceiver(aeroplane_change)
                }
            }) {
                Text(text = "Stop SMS Broadcast Service")
            }
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                custom_receive = CustomBroadcast()
                registerReceiver(
                    context,
                    custom_receive,
                    IntentFilter("com.example.broadcast.MY_CUSTOM_BROADCAST"),
                    ContextCompat.RECEIVER_EXPORTED
                )
            }) {
                Text(text = "Custom Broadcast")
            }
            Button(onClick = {
                val intent = Intent("com.example.broadcast.MY_CUSTOM_BROADCAST")
                intent.putExtra("message", "Custom message triggered")
                context.sendBroadcast(intent)
            }) {
                Text(text = "Get Message")
            }
        }
    }
}

}

fun isSmsPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        "android.permission.READ_SMS"
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Request runtime SMS permission
 */
private fun requestReadAndSendSmsPermission(currentActivity: MainActivity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, "android.permission.READ_SMS")) {
        // You may display a non-blocking explanation here, read more in the documentation:
        // https://developer.android.com/training/permissions/requesting.html
    }
    ActivityCompat.requestPermissions(
        currentActivity,
        arrayOf<String>("android.permission.READ_SMS", "android.permission.RECEIVE_SMS"),
        225
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestServiceTheme {
        Greeting("Android")
    }
}