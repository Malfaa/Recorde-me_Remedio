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

