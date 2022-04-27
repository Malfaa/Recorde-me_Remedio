package com.malfaa.lembrete

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.fragment.MainFragment

class AlarmReceiver(val remedio:String, val nota:String, val id: Int) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmIntent = Intent(context, MainFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,alarmIntent,0)  //Intent.FLAG_ACTIVITY_NEW_TASK

        val builder = NotificationCompat.Builder(context!!, "recorde-meremedio")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle("Titulo"/*remedio*//*remedio.value.toString()*/) //aqui
            .setContentText("Subtitulo"/*nota*//*nota.value. toString()*/) // aqui
            //.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)//alarmIntent

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id/*1*/, builder.build()) //fixme alterar o id p/ receber os valores do db

        Toast.makeText(MainFragment().requireContext(), "Alarme disparado", Toast.LENGTH_SHORT).show()
    }
}
