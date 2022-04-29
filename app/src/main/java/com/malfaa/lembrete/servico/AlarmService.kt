package com.malfaa.lembrete.servico

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.malfaa.lembrete.AlarmReceiver
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.requestRandomCode
import java.util.*

class AlarmService(private val context: Context){

    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var intent: Intent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun alarme(hora: Long, minutos: Long, horario: Long ){
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(context, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, requestRandomCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hora.toInt())
            set(Calendar.MINUTE, minutos.toInt())
        }

        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 /** (60 * horario)*/,  //60000 * (60 * 4) = 60000 * '240' = 144000000   setExactAndAllowWhileIdle()
            alarmIntent
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun removerAlarme(requestCode:Int){
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(context, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT) //todo aqui

        alarmMgr.cancel(alarmIntent)
    }
}