package com.malfaa.lembrete.servico

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.malfaa.lembrete.AlarmReceiver
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.requestRandomCode
import java.util.*

class AlarmService(private val context: Context){

    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var intent: Intent


    @SuppressLint("UnspecifiedImmutableFlag")
    fun alarme(hora: Long, minutos: Long, horario: Long ){
        try {
//            val _INTERVALO = 1000 * 60 * (60 * horario)

            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            intent = Intent(context, AlarmReceiver::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmIntent = PendingIntent.getBroadcast(
                    context,
                    requestRandomCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }else{
                alarmIntent = PendingIntent.getBroadcast(
                    context,
                    requestRandomCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            val calendar : Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hora.toInt())
                set(Calendar.MINUTE, minutos.toInt())
            }

////            if(Calendar.HOUR_OF_DAY == hora.toInt() && Calendar.MINUTE == minutos.toInt()){
//                if (Build.VERSION.SDK_INT >= 23) {
//                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                        _INTERVALO, alarmIntent)
//                } else {
//                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, _INTERVALO, alarmIntent)
//                }
////            }

//            intent.putExtra("Horario", horario)


            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                1000 * 60 * (60 * horario),  //60000 * (60 * 4) = 60000 * '240' = 144000000   setExactAndAllowWhileIdle()
                alarmIntent
            )
        }catch (e:Exception){
                Log.d("Error",e.toString())
            }
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun removerAlarme(requestCode:Int){
            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            intent = Intent(context, AlarmReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT) //todo aqui

            alarmMgr.cancel(alarmIntent)
        }
    }












/*lateinit var calendar: Calendar

            // se a hora for menor que a hora atual, soma mais um dia p/ ser colocado pro dia seguinte
            // o alarme
            if (hora.toInt() > Calendar.HOUR_OF_DAY && minutos.toInt() > Calendar.MINUTE) {//fixme aqui
                calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    this.add(Calendar.DATE, 1)
                    set(Calendar.HOUR_OF_DAY, hora.toInt())
                    set(Calendar.MINUTE, minutos.toInt())
                }
            } else {
                calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hora.toInt())
                    set(Calendar.MINUTE, minutos.toInt())
                }
            }*/