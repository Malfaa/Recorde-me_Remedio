package com.malfaa.lembrete.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.malfaa.lembrete.servico.AlarmService

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            AlarmService(context!!).schedulePushNotification()
        }
    }
}