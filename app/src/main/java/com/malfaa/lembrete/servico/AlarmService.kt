package com.malfaa.lembrete.servico

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.malfaa.lembrete.AlarmReceiver
import com.malfaa.lembrete.RandomUtil
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.requestRandomCode
import java.util.*

class AlarmService(private val context: Context){
//    private val alarmManager: AlarmManager =
//        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//
//    private fun setAlarm(){
//
//    }

    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var intent: Intent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun alarme(hora: Long, minutos: Long, horario: Long ){
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(context, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, requestRandomCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // TODO: Ideia, logo acima, pede p/ colocar o id, o id seria bom pegar o valor id inserido no bd, que assim é possível ter múltiplas notificações de lembretes. A ideia talvez seja pegar do bd o id, nome e nota.

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
//        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(
//            context, AlarmReceiver::class.java).let { intent ->
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

        alarmMgr.cancel(alarmIntent)
    }
}