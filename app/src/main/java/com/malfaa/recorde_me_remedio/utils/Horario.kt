package com.malfaa.recorde_me_remedio.utils

import android.annotation.SuppressLint
import com.malfaa.recorde_me_remedio.local.Remedio
import java.text.SimpleDateFormat
import java.util.*

object Horario {
    @SuppressLint("SimpleDateFormat")
    fun miliParaHoraMinuto(tempo: Long): String {
        val date = Date(tempo)
        val formatter = SimpleDateFormat("HH:mm")

        return formatter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun miliParaHoraMinuto(item: Remedio, horario: Long): String {
        return when(item.linguagem){
            "português" ->{
                val date = Date(horario)
                val formatter = SimpleDateFormat("HH:mm")

                formatter.format(date)
            }
            else->{
                val date = Date(horario)
                val formatter = SimpleDateFormat("hh:mm a")

                formatter.format(date)
            }
        }
    }
}