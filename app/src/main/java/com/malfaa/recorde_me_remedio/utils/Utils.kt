package com.malfaa.recorde_me_remedio.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
import com.malfaa.recorde_me_remedio.utils.Horario.miliParaHoraMinuto
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@SuppressLint("SimpleDateFormat")
fun calendarioParaData(item: Date): String {
    val formato = SimpleDateFormat("dd/MM")
    return formato.format(item)
}

@SuppressLint("SimpleDateFormat")
fun calendarioParaDataIngles(item: Date): String {
    val formato = SimpleDateFormat("MM/dd")
    return formato.format(item)
}

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------

@SuppressLint("SimpleDateFormat")
fun tempoEmMilissegundos(hora: Int, minuto: Int):Long {//"2014/10/29 18:10:45"
    return try {
        val myDate = "${diaAtualFormatado(hora,minuto)} $hora:$minuto:00" //1670344200000 = 13:30
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

fun testeSeODiaInicialSeraHojeOuAmanha(date: Long):Int{
    val horarioEscolhidoConcatenado = date + (1000 * 60 * 15) //15 min de atraso
    val horarioLocalConcatenado = System.currentTimeMillis()

    return if (horarioEscolhidoConcatenado > horarioLocalConcatenado) { //  19:15 19:00
        0
    }else{// 19:00 19:15
        1
    }
}
fun testeSeODiaInicialSeraHojeOuAmanha(hora:Int, minuto:Int):Int{
    val horarioEscolhidoConcatenado : String = "$hora" + "${minuto + 15}"//15 min de atraso
    val horarioLocalConcatenado : String = "${Calendar.HOUR_OF_DAY}" + "${Calendar.MINUTE}"

    // 11 + 30 + 15 = 56 não concatenado
    // 13 + 25 = 22

    return if (horarioEscolhidoConcatenado.toInt() > horarioLocalConcatenado.toInt()) { //  19:15 19:00
        0
    }else{// 19:00 19:15
        1
    }
}

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------


@SuppressLint("SimpleDateFormat")
fun diaAtualFormatado(horaInit: Int, minutoInit: Int):String{
    val calendar = Calendar.getInstance()
    if(testeSeODiaInicialSeraHojeOuAmanha(horaInit, minutoInit) == 1) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    val formato = SimpleDateFormat("yyyy/MM/dd")
    return formato.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun diaInicial(date: Long, language: String, proxDia: Boolean):String {//"2014/10/29 18:10:45"
    val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, miliParaHoraMinuto(date).substringBefore(":").toInt())
        set(Calendar.MINUTE, miliParaHoraMinuto(date).substringAfter(":").toInt())
        if (proxDia){
            this.add(Calendar.DAY_OF_MONTH, 1)
        }
    }
    return try {
        val sdf: SimpleDateFormat = when(language){
            "português" -> SimpleDateFormat("dd/MM")
            else -> SimpleDateFormat("MM/dd")
        }
        sdf.format(calendar.time)

    }catch (e: Exception){
        Log.e("Error", e.message!!)
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun diaFinal(editTextData: String, date: Long, language: String, proxDia: Boolean):String {//"2014/10/29 18:10:45"
    val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, miliParaHoraMinuto(date).substringBefore(":").toInt())
        set(Calendar.MINUTE, miliParaHoraMinuto(date).substringAfter(":").toInt() )
        if (proxDia){
            this.add(Calendar.DAY_OF_MONTH, 1)
        }

        this.add(Calendar.DAY_OF_MONTH, editTextData.toInt())
    }
    return try {
        val sdf: SimpleDateFormat = when(language){
            "português" -> SimpleDateFormat("dd/MM")
            else -> SimpleDateFormat("MM/dd")
        }
        sdf.format(calendar.time)

    }catch (e: Exception){
        Log.e("Error", e.message!!)
        ""
    }
}

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------

fun timePicker(frag: FragmentManager, linguagem: String, text: TextView, diaReferencia: TextView) {
    var horaFinal: String
    val picker =
        when(linguagem){
            "português" ->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Hora em que iniciará:")
                    .setHour(LocalDateTime.now().hour)
                    .setMinute(LocalDateTime.now().minute)
                    .build()
                } else {
                    MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Hora em que iniciará:").setHour(miliParaHoraMinuto(System.currentTimeMillis()).substringBefore(":").toInt())//horaFormato(Date().time)
                        .setMinute(miliParaHoraMinuto(System.currentTimeMillis()).substringAfter(":").toInt())//minutoFormato(Date().time)
                        .build()
                }}
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
                    .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
                    .setMinute(LocalDateTime.now().minute)
                    .build()
                } else {
                    MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
                        .setTitleText("Hora em que iniciará:").setHour(miliParaHoraMinuto(System.currentTimeMillis()).substringBefore(":").toInt())//horaFormato(Date().time)
                        .setMinute(miliParaHoraMinuto(System.currentTimeMillis()).substringAfter(":").toInt())//minutoFormato(Date().time)
                        .build()

                }}
        }


    picker.show(frag, "remedio")

    picker.addOnPositiveButtonClickListener {
        AdicionarViewModel.horaInicial = String.format("%02d", picker.hour)
        AdicionarViewModel.minutoInicial = String.format("%02d", picker.minute)
        horaFinal = "${AdicionarViewModel.horaInicial}:${AdicionarViewModel.minutoInicial}"

        text.text = horaFinal
        diaReferencia.text = diaInicial(tempoEmMilissegundos(AdicionarViewModel.horaInicial.toInt(), AdicionarViewModel.minutoInicial.toInt()), linguagem, false)
    }
}