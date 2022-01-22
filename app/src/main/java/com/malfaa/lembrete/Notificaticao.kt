package com.malfaa.lembrete

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.fragment.MainFragment

class Notificaticao : BroadcastReceiver(){

    private lateinit var alarmIntent: PendingIntent

    override fun onReceive(context: Context?, p1: Intent?) {
        alarmIntent = Intent(
            context, MainFragment::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        val builder = NotificationCompat.Builder(context!!, "notificacao")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle("Lembrete")
            //.setContentInfo()
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(alarmIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())

    }
}
