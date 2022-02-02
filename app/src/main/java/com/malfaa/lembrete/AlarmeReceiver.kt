package com.malfaa.lembrete

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.nota
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.remedio
import com.malfaa.lembrete.fragment.MainFragment

class AlarmeReceiver : BroadcastReceiver(){

    private lateinit var alarmIntent: PendingIntent

    override fun onReceive(context: Context?, intent: Intent?) {
        alarmIntent = Intent(
            context, MainFragment::class.java).let {  // FIXME: fragment do alarme receiver -> arrumar o contexto que não está sendo attached
            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            PendingIntent.getBroadcast(context, 0, intent!!, 0)
        }

        val builder = NotificationCompat.Builder(context!!, "notificacao")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle(remedio.value.toString())
            .setContentText(nota.value. toString())
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(alarmIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())

        Toast.makeText(MainFragment().requireContext(), "Alarme disparado", Toast.LENGTH_SHORT).show()
    }
}