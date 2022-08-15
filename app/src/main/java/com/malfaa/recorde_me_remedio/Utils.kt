package com.malfaa.recorde_me_remedio

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object RandomUtil{
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()
}

fun testeSeODiaInicialSeraHojeOuAmanha(hora:Int, minuto:Int):Int{
    val horarioEscolhidoConcatenado = hora+minuto
    val horarioLocalConcatenado =
        Calendar.HOUR_OF_DAY + Calendar.MINUTE

    return if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
        1
    }else{
        0
    }
}

fun calendario(item: Int, verifica: Boolean): String{
    val calendario = Calendar.getInstance()
    val recebeValorEConfigura: Int

    val valorSelecionado: Int =
        when (item) {
            1 -> 5
            2 -> 7
            3 -> 14
            else -> 0
        }

    recebeValorEConfigura = when(verifica){
        true -> valorSelecionado
        false -> item
    }

    calendario.add(Calendar.DATE, recebeValorEConfigura)
    return calendarioParaData(calendario.time)
}

fun retornaHoraDataMetodo(verificaCustom:Boolean, spinner: Spinner, text:EditText): Int{
    return when(verificaCustom){
        true -> text.text.toString().toInt()
        false -> spinner.selectedItemPosition
    }
}

fun diaAtual():String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, testeSeODiaInicialSeraHojeOuAmanha(AdicionarViewModel.horaInicial.toInt(), AdicionarViewModel.minutoInicial.toInt()))
    return calendarioParaData(calendar.time)
}

fun diaFinal(verificaCustomData: Boolean, spinnerData: Spinner, editTextData: EditText){
    AdicionarViewModel.diaFinal = calendario(retornaHoraDataMetodo(verificaCustomData, spinnerData, editTextData),verificaCustomData)
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

