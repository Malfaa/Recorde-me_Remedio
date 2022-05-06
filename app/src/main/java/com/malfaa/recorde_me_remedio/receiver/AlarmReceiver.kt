package com.malfaa.recorde_me_remedio.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.R
import com.malfaa.recorde_me_remedio.fragment.AdicionarFragment.Companion.requestRandomCode

class AlarmReceiver: BroadcastReceiver(){
    private lateinit var alarmPendingIntent: PendingIntent

    override fun onReceive(context: Context?, intent: Intent?) {

        alarmPendingIntent = PendingIntent.getBroadcast(context, requestRandomCode, intent!!, 0)

        val builder = NotificationCompat.Builder(context!!, "recorde-meremedio")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle("Hora do Remédio!")
            .setContentText("Abra o app e veja qual remédio está para este horário.")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(alarmPendingIntent)
        //.setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())

    }
}
