package com.malfaa.lembrete

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.room.entidade.ItemEntidade
import java.util.*

private var alarmMgr: AlarmManager? = null
private lateinit var alarmIntent: PendingIntent


//@SuppressLint("UnspecifiedImmutableFlag")
//fun alarme(hora: Long, minutos: Long, horario: Long) { //mainviewmodel    horario é em millis, logo, valor tem que ser long
//    alarmMgr = mainInstance().requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    alarmIntent = Intent(mainInstance().requireContext(), MainFragment::class.java).let { intent ->
//        PendingIntent.getBroadcast(mainInstance().requireContext(), 0, intent, 0)
//    }
//
//    // Set the alarm to start at 8:30 a.m.
//    val calendar: Calendar = Calendar.getInstance().apply {
//        timeInMillis = System.currentTimeMillis()
//        set(Calendar.HOUR_OF_DAY, hora.toInt())
//        set(Calendar.MINUTE, minutos.toInt())
//    }
//
//    // setRepeating() lets you specify a precise custom interval--in this case,
//    // 20 minutes.
//    alarmMgr?.setRepeating(
//        AlarmManager.RTC_WAKEUP,
//        calendar.timeInMillis,
//        1000 * 60 * horario,  //60000 * (60 * 4) = 60000 * '240' = 144000000
//        alarmIntent
//    )
//}

fun cancelarAlarme(){
    alarmMgr?.cancel(alarmIntent)
}

fun conversorStringEmMinutos(valor: String): Long{
    return when(valor){
        "4 em 4 horas" -> 240
        "6 em 6 horas" -> 360
        "8 em 8 horas" -> 480
        "12 em 12 horas"-> 720
        "24 em 24 horas" -> 1440
        "Customizar..." -> 1
        else -> 0
    }
}

fun conversorStringEmId(valor: String): Int{
    return when(valor){
        "4 em 4 horas" -> 1
        "6 em 6 horas" -> 2
        "8 em 8 horas" -> 3
        "12 em 12 horas"-> 4
        "24 em 24 horas" -> 5
        "Customizar..." -> 6
        else -> 0
    }
}

@BindingAdapter("setRemedio")
fun TextView.setRemedio(item: String){
    text = item
}

@BindingAdapter("setHorario")
fun TextView.setHorario(item: String){
    text = item
}

@BindingAdapter("setData")
fun TextView.setData(item: String){
    text = item
}

@BindingAdapter("setNota")
fun TextView.setNota(item: String){
    text = item
}


// FIXME: 20/01/2022 adicionei vários setters no "item_lembrete.xml"
//todo https://www.google.com/search?client=firefox-b-d&q=kotlin+notification+
//todo https://www.google.com/search?client=firefox-b-d&q=kotlin+alarm+
//todo kotlin alarm manager how to set multiple alarms