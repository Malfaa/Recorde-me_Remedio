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

@SuppressLint("SimpleDateFormat")
fun miliParaHoraMinuto(tempo: Long): String {
    val date = Date(tempo)
    val formatter = SimpleDateFormat("HH:mm")

    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun calendarioParaData(item: Date): String {
    val formato = SimpleDateFormat("dd/MM")
    return formato.format(item)
}

@SuppressLint("SimpleDateFormat")
fun tempoEmMilissegundos(hora: Int, minuto: Int):Long {//"2014/10/29 18:10:45"
    return try {
        val myDate = "${diaAtualFormatado()} $hora:$minuto:00"
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date: Date= sdf.parse(myDate) as Date

        date.time
    }catch (e: Exception){
        Log.e("Error", e.message!!)
        0L
    }
}

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------

fun testeSeODiaInicialSeraHojeOuAmanha(date: Long):Int{ //talvez pra saber se soma ou não
    val horarioEscolhidoConcatenado = date + (1000 * 60 * 15) //15 min de atraso
    val horarioLocalConcatenado = System.currentTimeMillis()

    return if (horarioEscolhidoConcatenado > horarioLocalConcatenado) { //  19:15 19:00
        0
    }else{// 19:00 19:15
        1
    }
}
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

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------


@SuppressLint("SimpleDateFormat")
fun diaAtualFormatado():String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(AdicionarViewModel.horaInicial.toInt(), AdicionarViewModel.minutoInicial.toInt()))

    val formato = SimpleDateFormat("yyyy/MM/dd")
    return formato.format(calendar.time)
}

fun diaAtual(tempo: Long):String{
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(tempo))
    return calendarioParaData(calendario.time)
//    return calendarioParaData(dateParaDiasAtuaisEFinais(tempo))
}

fun diaFinal(editTextData: String, tempo:Long):String{
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(tempo)+editTextData.toInt())

    return calendarioParaData(calendario.time)
}

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------


fun picker(frag: FragmentManager, text: TextView) {
    var horaFinal: String
    val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
            .setMinute(LocalDateTime.now().minute).build()
    } else {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Hora em que iniciará:").setHour(miliParaHoraMinuto(System.currentTimeMillis()).substringBefore(":").toInt())//horaFormato(Date().time)
            .setMinute(miliParaHoraMinuto(System.currentTimeMillis()).substringAfter(":").toInt()).build()//minutoFormato(Date().time)
    }

    picker.show(frag, "remedio")

    picker.addOnPositiveButtonClickListener {
        AdicionarViewModel.horaInicial = String.format("%02d", picker.hour)
        AdicionarViewModel.minutoInicial = String.format("%02d", picker.minute)
        horaFinal = "${AdicionarViewModel.horaInicial}:${AdicionarViewModel.minutoInicial}"

        text.text = horaFinal
    }
}