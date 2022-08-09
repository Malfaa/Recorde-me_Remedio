package com.malfaa.recorde_me_remedio

import android.os.Build
import android.util.Log
import androidx.databinding.BindingAdapter
import java.time.LocalDate
import java.util.*

@BindingAdapter("data", "verificaData", requireAll = true)
fun conversorPosEmData(data: Int, verificaData: Boolean): String? {
    return if(!verificaData){
        when(data){
            1 -> "5 dias"
            2 -> "7 dias"
            3-> "14 dias"
            4-> "Todos os dias"
            else -> null
        }
    }else{
        "$data dias"
    }
}

@BindingAdapter("hora", "verificaHora", requireAll = true)
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

@BindingAdapter("calendario", "verificaCalendario", requireAll = true)
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

//@BindingAdapter("notaView")
//fun notaView(view: View, nota: Boolean?){
//    if (item.nota.isEmpty()){
//                binding.expand.visibility = View.GONE
//            }else{
//                binding.nota.text = item.nota
//            }
//}