package com.malfaa.recorde_me_remedio.alarme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.os.bundleOf
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_ACTION
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE

class RebootService(private val context: Context) {

    private lateinit var alarmManager: AlarmManager
    private lateinit var notifyIntent : Intent
    private lateinit var notifyPendingIntent: PendingIntent
    private val database = RemedioDatabase.getInstance(context)

    @SuppressLint("UnspecifiedImmutableFlag")
    suspend fun reboot(){
        val remedios = database.dao.getRemedioReboot()

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (i in remedios){
            notifyIntent = Intent(context, AlarmeReceiver::class.java).apply {
                action = INTENT_ACTION // ou INTENT_REBOOT
                putExtra(INTENT_BUNDLE, bundleOf(INTENT_BUNDLE to i))
            }
            notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(
                    context.applicationContext,
                    i.requestCode,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }else{
                PendingIntent.getBroadcast(
                    context.applicationContext,
                    i.requestCode,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            val horaFinal = (System.currentTimeMillis() - i.horaComecoEmMillis) //1667345544000 - 1667339700000 = 5844000

            when(i.horaComecoEmMillis > System.currentTimeMillis()){
                true -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() - horaFinal, //1667345544000 -(-5844000) = horario inicial correto
                            notifyPendingIntent
                        )
                    }else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() - horaFinal,
                            notifyPendingIntent
                        )
                    }
                }
                false -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + ((1000 * 60 * (60 * i.horaEmHora).toLong()) - horaFinal), //28800000 = 8 horas
                            notifyPendingIntent  // 1667345544000 + 22956000
                        )
                    }else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + ((1000 * 60 * (60 * i.horaEmHora).toLong()) - horaFinal),
                            notifyPendingIntent
                        )
                    }
                }
            }
        }
    }
}
////System.currentTimeMillis() + ((i.horaComeco.toLong() + (1000 * 60 * (60 * i.horaEmHora).toLong()) - System.currentTimeMillis()))
// Tue Nov 01 2022 20:32:24     -> HORA ATUAL
// Tue Nov 01 2022 18:55:00     -> HORA COMECO
// Wed Nov 02 2022 02:55:00     -> HORA REMARCADA