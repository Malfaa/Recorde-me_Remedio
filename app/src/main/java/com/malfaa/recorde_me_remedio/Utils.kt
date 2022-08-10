package com.malfaa.recorde_me_remedio

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

fun calendario(item: Int, verifica: Boolean): String? {
    return if (!verifica) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val diaAtual = LocalDate.now()
            Log.d("28+", "SDK superior")
            when (item) {
                1/*"5 dias"*/ -> stringFormat(diaAtual.plusDays(5).dayOfMonth) + "/" + stringFormat(diaAtual.plusDays(5).monthValue)
                2/*"7 dias"*/ -> stringFormat(diaAtual.plusDays(7).dayOfMonth) + "/" + stringFormat(diaAtual.plusDays(7).monthValue)
                3/*"14 dias"*/-> stringFormat(diaAtual.plusDays(14).dayOfMonth) + "/" + stringFormat(diaAtual.plusDays(14).monthValue)
                //"Todos os dias" -> diaAtual.plusDays(5) //talvez criar alguma var que altere um fun que daí escreve os diaAtual
                else -> null
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
            return calendarioParaData(calendario.time)
        }
    } else {
        val calendario = Calendar.getInstance()

        calendario.add(Calendar.DATE, item)
        return calendarioParaData(calendario.time)
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
    } catch (e: Exception) {
        Log.d("error", "$e")
    }
}

