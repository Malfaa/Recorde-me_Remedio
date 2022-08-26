package com.malfaa.recorde_me_remedio.alarme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.malfaa.recorde_me_remedio.local.Remedio

class AlarmeService {

    private lateinit var alarmManager: AlarmManager
    private lateinit var notifyIntent : Intent
    private lateinit var notifyPendingIntent: PendingIntent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun adicionarAlarme(context:Context, item: Remedio) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            notifyIntent = Intent(context, AlarmeReceiver::class.java)

            notifyPendingIntent = PendingIntent.getBroadcast(
                context.applicationContext,
                item.requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    100000,
                    notifyPendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    100000,
                    notifyPendingIntent
                )

        }// TODO: Corrigir o tempo e colocar reschedule 
    }

    // TODO: verificar se mesmo fechado funciona, colocar reboot permission tbm 

    // TODO: criar vários alarmes
    @SuppressLint("UnspecifiedImmutableFlag")
    fun removerAlarme(context:Context, item: Remedio){
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notifyIntent = Intent(context, AlarmeReceiver::class.java)

        notifyPendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            item.requestCode,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(notifyPendingIntent)
    }
}