package com.malfaa.lembrete

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object RandomUtil{
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()
}

fun conversorPosEmMinutos(pos: Int): Long? {
    return when(pos){
        1 -> 4//240
        2 -> 6//360
        3 -> 8//480
        4 -> 12//720
        5 -> 24//1440
        else -> null
    }
}

fun conversorPosEmData(valor: Int, verifica: Boolean): String? {
    return if(!verifica){
        when(valor){
            1 -> "5 dias"
            2 -> "7 dias"
            3-> "14 dias"
            4-> "Todos os dias"
            else -> null
        }
    }else{
        "$valor dias"
    }
}
fun conversorPosEmHoras(valor: Int, verifica: Boolean): String? {
    return if(!verifica){
        when(valor){
            //0 -> null
            1 -> "4 em 4 horas"
            2 -> "6 em 6 horas"
            3 -> "8 em 8 horas"
            4 -> "12 em 12 horas"
            5 -> "24 em 24 horas"
            else -> null
        }
    }else{
        "$valor em $valor horas"
    }
}

// TODO: ao invés de verificar um por um, usar uma lista que mostra se todos os campos foram preenchidos

fun calendario(item: Int, verifica: Boolean): String {
    return if (!verifica) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val diaAtual = LocalDate.now()
            Log.d("28+", "SDK superior")
            when (item) {
                1/*"5 dias"*/ -> stringFormat(diaAtual.plusDays(5).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(5).monthValue)
                2/*"7 dias"*/ -> stringFormat(diaAtual.plusDays(7).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(7).monthValue)
                3/*"14 dias"*/-> stringFormat(diaAtual.plusDays(14).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(14).monthValue)
                //"Todos os dias" -> diaAtual.plusDays(5) //talvez criar alguma var que altere um fun que daí escreve os diaAtual
                else -> ""
            }
        }
        else {
            Log.d("28-", "SDK inferior")
            val calendario = Calendar.getInstance()

            val valorSelecionado: Int =
                when (item) {
                    1 -> 5
                    2 -> 7
                    3 -> 14
                    else -> 0
                }
//------------------------------------  -------------------
            calendario.add(Calendar.DATE, valorSelecionado)
            calendarioParaData(calendario.time)
        }
    } else {
        val calendario = Calendar.getInstance()

        calendario.add(Calendar.DATE, item)
        calendarioParaData(calendario.time)
    }
}

@SuppressLint("SimpleDateFormat")
fun calendarioParaData(item: Date): String {
    val formato = SimpleDateFormat("dd/MM")
    return formato.format(item)
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

}@SuppressLint("SimpleDateFormat")
fun minutoFormato(horaSistema: Long): Int {
    return try {
        val formato = SimpleDateFormat("mm")
        formato.format(horaSistema).toInt()
    }catch (e: Exception) {
        Log.d("error", "$e")
    }


}