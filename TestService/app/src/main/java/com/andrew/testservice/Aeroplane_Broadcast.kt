package com.andrew.testservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class Aeroplane_Broadcast : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
                val isAirplaneModeOn = intent!!.getBooleanExtra("state", false)
                if (isAirplaneModeOn) {
                        Toast.makeText(context, "AirplaneMode is ON", Toast.LENGTH_SHORT).show();
                } else {
                        Toast.makeText(context, "AirplaneMode is OFF", Toast.LENGTH_SHORT).show();
                }

        }
}