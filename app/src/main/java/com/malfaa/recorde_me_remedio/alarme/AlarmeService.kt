package com.malfaa.recorde_me_remedio.alarme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.os.bundleOf
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_ACTION
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE
import java.util.*

class AlarmeService {

    private lateinit var alarmManager: AlarmManager
    private lateinit var notifyIntent : Intent
    private lateinit var notifyPendingIntent: PendingIntent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun adicionarAlarme(context:Context, item: Remedio, valor: Int?) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notifyIntent = Intent(context, AlarmeReceiver::class.java).apply {
            action = INTENT_ACTION
            putExtra(INTENT_BUNDLE, bundleOf(INTENT_BUNDLE to item))
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, item.horaComeco.substringBefore(":").toInt()) //item.horaComeco.substringBefore(":").toInt()
            set(Calendar.MINUTE, item.horaComeco.substringAfter(":").toInt() )//item.horaComeco.substringAfter(":").toInt()
        }


        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context.applicationContext,
                item.requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }else{
            PendingIntent.getBroadcast(
                context.applicationContext,
                item.requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        when(valor){
            null -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        notifyPendingIntent
                    )
                }else{
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        notifyPendingIntent
                    )
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (1000 * 60 * (60 * valor).toLong()), //pega o agora e soma equação
                        notifyPendingIntent
                    )

                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (1000 * 60 * (60 * valor).toLong()),//1000 * 60 * (60 * item.horaEmHora).toLong(),
                        notifyPendingIntent
                    )
                }
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun removerAlarme(context:Context, item: Remedio){
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notifyIntent = Intent(context, AlarmeReceiver::class.java).apply {
            action = INTENT_ACTION
        }

        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context.applicationContext,
                item.requestCode,
                notifyIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE //FLAG_UPDATE_CURRENT
            )
        }else{
            PendingIntent.getBroadcast(
                context.applicationContext,
                item.requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmManager.cancel(notifyPendingIntent)
    }
}