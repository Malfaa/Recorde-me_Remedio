package com.malfaa.recorde_me_remedio.alarme

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_ACTION
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.notificacao.sendNotification


class AlarmeReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (intent.action == INTENT_ACTION) {
            val item = intent.getBundleExtra(INTENT_BUNDLE)?.getParcelable<Remedio>(INTENT_BUNDLE)

            notificationManager.sendNotification(context, item!!)
            AlarmeService().removerAlarme(context, item)
            AlarmeService().adicionarAlarme(context, item, item.horaEmHora)

        }
    }
}