package com.malfaa.lembrete.servico

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.malfaa.lembrete.conversorPosEmMinutos
import com.malfaa.lembrete.receiver.AlarmReceiver
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.lembretes
import kotlinx.coroutines.*
import java.util.*


class AlarmService(private val context: Context ) {
    private lateinit var alarmMgr           :AlarmManager
    private lateinit var alarmPendingIntent :PendingIntent
    private lateinit var intent             :Intent

    private val job     = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)



    @SuppressLint("UnspecifiedImmutableFlag")
    fun removerAlarme(requestCode: Int) {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(context, AlarmReceiver::class.java)
        alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) //todo aqui

        alarmMgr.cancel(alarmPendingIntent)
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun schedulePushNotification() {
        uiScope.launch {
            delay(1000)

            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            intent = Intent(context, AlarmReceiver::class.java)

            for (item in lembretes) {
                Log.d("FOR", "Passou ${item.id}")
                alarmPendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
                )

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, item.horaInicial.take(2).toInt()/*horas[item.hora]*/)
                    set(Calendar.MINUTE, item.horaInicial.takeLast(2).toInt()/*minutos[item.hora]*/)
                }

                delay(500)

                if (item.verificaHoraCustom) {
                    alarmMgr.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        1000 * 60 * (60 * conversorPosEmMinutos(item.hora)!!/*horario*/),
                        alarmPendingIntent
                    )
                    Log.d("Alarm", "Passou Spinner")
                } else {
                    alarmMgr.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        1000 * 60 * (60 * item.hora.toLong())/*horario*/,
                        alarmPendingIntent
                    )
                }
            }
        }
    }}