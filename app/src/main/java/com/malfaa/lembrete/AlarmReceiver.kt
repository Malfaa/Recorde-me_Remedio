package com.malfaa.lembrete

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.nota
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.remedio
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.requestRandomCode
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.repository.ItemRepository

class AlarmReceiver: BroadcastReceiver(){
    @SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmIntent = Intent(context, MainFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context,
            requestRandomCode,alarmIntent,PendingIntent.FLAG_ONE_SHOT and PendingIntent.FLAG_NO_CREATE)

        val builder = NotificationCompat.Builder(context!!, "recorde-meremedio")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle(remedio.value.toString())
            .setContentText(nota.value.toString())
            //problema p/ ele retornar null é, quando ele vai chamar a função, as variáveis já perderam
                // seu value, logo, retorna null
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }
}
