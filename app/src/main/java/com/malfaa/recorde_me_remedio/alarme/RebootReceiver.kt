package com.malfaa.recorde_me_remedio.alarme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RebootReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            RebootService(context).reboot()
        }
    }
}