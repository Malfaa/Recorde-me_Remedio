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

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hora.toInt())
            set(Calendar.MINUTE, minutos.toInt())
        }

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 /** (60 * horario)*/,  //60000 * (60 * 4) = 60000 * '240' = 144000000   setExactAndAllowWhileIdle()
            alarmIntent
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun removerAlarme(requestCode:Int){
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(
//            context, AlarmReceiver::class.java).let { intent ->
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
        intent = Intent(context, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT) //todo aqui
        // aqui o que precisa é ele receber o mesmo pending intent do qual foi salvo, ele funciona
        // por meio de um token que diferencia um intent do outro, então o que preciso fazer é
        // pra quando for apagar o alarme, usar o mesmo pendingintent que usei anteriormente

        alarmMgr.cancel(alarmIntent)
    }
}

/*alarmIntent = Intent(
//            requireContext(), AlarmReceiver(remedio.value.toString(),nota.value.toString(), 1)::class.java).let { intent ->
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            PendingIntent.getActivity(requireContext(), 0, intent, 0)
//        }*/