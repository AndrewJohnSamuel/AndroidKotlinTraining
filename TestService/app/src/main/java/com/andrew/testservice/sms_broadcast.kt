package com.andrew.testservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


class sms_broadcast : BroadcastReceiver() {
    val SMS_RECEIVED: String = "android.provider.Telephony.SMS_RECEIVED"
    val TAG: String = "SMSBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,  "Intent recieved: " + intent!!.action, Toast.LENGTH_LONG).show()

        if (intent!!.action === SMS_RECEIVED) {
            val bundle = intent!!.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
                for (i in pdus!!.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus!![i] as ByteArray)
                }
                if (messages.size > -1) {
                    Toast.makeText(context, "Message recieved: " + messages[0]?.getMessageBody(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}