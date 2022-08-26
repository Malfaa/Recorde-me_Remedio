package com.malfaa.recorde_me_remedio.alarme

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.malfaa.recorde_me_remedio.notificacao.sendNotification
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel.Companion.item_para_alarme


class AlarmeReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            item_para_alarme.value!!,
            context
        )
    }

}