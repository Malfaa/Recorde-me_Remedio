package com.malfaa.recorde_me_remedio.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

fun testeSeODiaInicialSeraHojeOuAmanha(hora:Int, minuto:Int):Int{
    val horarioEscolhidoConcatenado = hora+minuto +15 //15 min de atraso
    val horarioLocalConcatenado =
        Calendar.HOUR_OF_DAY + Calendar.MINUTE

    return if (horarioEscolhidoConcatenado > horarioLocalConcatenado) { //  19:15 19:00
        0
    }else{// 19:00 19:15
        1
    }
}

fun calendario(item: Int): String{
    val calendario = Calendar.getInstance()

    calendario.add(Calendar.DATE, item)
    return calendarioParaData(calendario.time)
}

fun diaAtual():String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(AdicionarViewModel.horaInicial.toInt(), AdicionarViewModel.minutoInicial.toInt()))
    return calendarioParaData(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun diaAtualFormatado():String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(AdicionarViewModel.horaInicial.toInt(), AdicionarViewModel.minutoInicial.toInt()))

    val formato = SimpleDateFormat("yyyy/MM/dd")
    return formato.format(calendar.time)
}

fun diaFinal(editTextData: String):String{
    return calendario(editTextData.toInt())
}

@SuppressLint("SimpleDateFormat")
fun calendarioParaData(item: Date): String {
    val formato = SimpleDateFormat("dd/MM")
    return formato.format(item)
}

fun picker(frag: FragmentManager, text: TextView) {
    var horaFinal: String
    val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
            .setMinute(LocalDateTime.now().minute).build()
    } else {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Hora em que iniciará:").setHour(horaFormato(Date().time))
            .setMinute(minutoFormato(Date().time)).build()
    }

    picker.show(frag, "remedio")

    picker.addOnPositiveButtonClickListener {
        AdicionarViewModel.horaInicial = String.format("%02d", picker.hour)
        AdicionarViewModel.minutoInicial = String.format("%02d", picker.minute)
        horaFinal = "${AdicionarViewModel.horaInicial}:${AdicionarViewModel.minutoInicial}"

        text.text = horaFinal
    }
}

fun stringFormat(dia:Int):String{
    return String.format("%02d",dia)
}

@SuppressLint("SimpleDateFormat")
fun horaFormato(horaSistema: Long): Int {
    return try {
        val formato = SimpleDateFormat("HH:mm")
        formato.format(horaSistema).toInt()
    }catch (e: Exception) {
        Log.d("error", "$e")
    }

}
@SuppressLint("SimpleDateFormat")
fun minutoFormato(horaSistema: Long): Int {
    return try {
        val formato = SimpleDateFormat("mm")
        formato.format(horaSistema).toInt()
    } catch (e: Exception) {
        Log.d("error", "$e")
    }
}

@SuppressLint("SimpleDateFormat")
fun tempoEmMilissegundos(hora: Int, minuto: Int):Long {//"2014/10/29 18:10:45"
    val myDate = "${diaAtualFormatado()} $hora:$minuto:00"
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val date: Date= sdf.parse(myDate) as Date

    return date.time * 1000
}

@SuppressLint("SimpleDateFormat")
fun minutoParaAlarme(horaSistema: Long): Int {
    return try {
        val formato = SimpleDateFormat("HH:mm")
        formato.format(horaSistema).substringAfter(":").toInt()
    }catch (e: Exception) {
        Log.d("error", "$e")
    }
}