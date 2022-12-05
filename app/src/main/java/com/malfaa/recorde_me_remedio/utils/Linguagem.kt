package com.malfaa.recorde_me_remedio.utils

import android.util.Log
import java.util.*

object Linguagem {
    fun linguagem(calendario: Date, local: String): String{
        Log.d("Linguagem", local)
        return when(local){
            "português" -> calendarioParaData(calendario)
            else -> calendarioParaDataIngles(calendario)
        }
    }
}