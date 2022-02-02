package com.malfaa.lembrete

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private var alarmMgr: AlarmManager? = null
private lateinit var alarmIntent: PendingIntent

fun cancelarAlarme(){
    alarmMgr?.cancel(alarmIntent)
}

fun conversorPosEmMinutos(pos: Int): Long{
    return when(pos){
        0 -> 240
        1 -> 360
        2 -> 480
        3-> 720
        4 -> 1440
        5 -> 1
        else -> 0
    }
}

fun conversorPosEmData(valor: Int): String{        //3-> "1 semana"
    return when(valor){
        1 -> "5 dias"
        2 -> "7 dias"
        3-> "14 dias"
        4-> "Todos os dias"
        5-> "Customizar..."
        else -> ""
    }
}
fun conversorPosEmHoras(valor: Int): String{
    return when(valor){
        1 -> "4 em 4 horas"
        2 -> "6 em 6 horas"
        3-> "8 em 8 horas"
        4-> "12 em 12 horas"
        5-> "24 em 24 horas"
        6-> "Customizar..."
        else -> ""
    }
}

fun calendario(item: Int): String {//"1 semana" -> diaAtual.plusDays(7) //"2 semanas" -> diaAtual.plusDays(14)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val diaAtual = LocalDate.now()
        Log.d("Nice", "SDK superior")
        when(item){
            1/*"5 dias"*/ -> "${diaAtual.plusDays(5).dayOfMonth} / ${diaAtual.plusDays(5).monthValue}"
            2/*"7 dias"*/ -> diaAtual.plusDays(7).toString()
            3/*"14 dias"*/ -> diaAtual.plusDays(14).toString()
            //"Todos os dias" -> diaAtual.plusDays(5) //talvez criar alguma var que altere um fun que daí escreve os diaAtual
            else -> ""
        }
    }else{
        Log.d("Bosta", "SDK inferior")
        val calendario = Calendar.getInstance()

        val valorSelecionado:Int =
            when(item){
                1 -> 5
                2 -> 7
                3 -> 14
                else -> 0
        }

        calendario.add(Calendar.DATE, valorSelecionado)
        calendarioParaData(calendario.time)
    }
}

@SuppressLint("SimpleDateFormat")
fun calendarioParaData(item: Date): String {
    val formato = SimpleDateFormat("dd/MM")
    return formato.format(item)
}

fun relogio(): MaterialTimePicker {
    val picker: MaterialTimePicker =
    MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText("Hora em que iniciará:").setHour(12)
        .setMinute(0).build()

    return picker
}

@SuppressLint("SimpleDateFormat")
fun dataFormato(horaSistema: Long): String {
    return try {
        val formato = SimpleDateFormat("HH:mm")
        formato.format(horaSistema)
    }catch (e: Exception) {
        e.toString()
    }
}