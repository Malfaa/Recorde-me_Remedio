package com.malfaa.recorde_me_remedio

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import java.time.LocalDate
import java.util.*

@BindingAdapter("conversorPosEmData", "verificaData", requireAll = true)
fun conversorPosEmData(textView : TextView, data: Int, verificaData: Boolean) {
    if(!verificaData){
        when(data){
            1 -> textView.text = "5 dias"
            2 -> textView.text = "7 dias"
            3-> textView.text = "14 dias"
            4-> textView.text = "Todos os dias"
        }
    }else{
        textView.text = "$data dias"
    }
}

@BindingAdapter("conversorPosEmHoras", "verificaHora", requireAll = true)
fun conversorPosEmHoras(textView : TextView, valor: Int, verifica: Boolean) {
    if(!verifica){
        when(valor){
            //0 -> null
            1 -> textView.text = "4 em 4 horas"
            2 -> textView.text = "6 em 6 horas"
            3 -> textView.text = "8 em 8 horas"
            4 -> textView.text = "12 em 12 horas"
            5 -> textView.text = "24 em 24 horas"
        }
    }else{
        textView.text = "$valor em $valor horas"
    }
}

@BindingAdapter("calendario", "verificaCalendario", requireAll = true)
fun calendario(textView : TextView, item: Int, verifica: Boolean){
    if (!verifica) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val diaAtual = LocalDate.now()
            Log.d("28+", "SDK superior")
            when (item) {
                1/*"5 dias"*/ -> stringFormat(diaAtual.plusDays(5).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(5).monthValue)
                2/*"7 dias"*/ -> stringFormat(diaAtual.plusDays(7).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(7).monthValue)
                3/*"14 dias"*/-> stringFormat(diaAtual.plusDays(14).dayOfMonth)+ "/" + stringFormat(diaAtual.plusDays(14).monthValue)
                //"Todos os dias" -> diaAtual.plusDays(5) //talvez criar alguma var que altere um fun que daí escreve os diaAtual
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
        textView.text = calendarioParaData(calendario.time)
    }
}

@BindingAdapter("notaValue")
fun notaView(view:View, nota:String){
    if (nota.isEmpty()){
        view.visibility = View.GONE
    }
}

@BindingAdapter("clicks")
fun listenClicks(spinner: AppCompatSpinner, result: ObservableField<Int>) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            result.set(parent?.getItemAtPosition(position) as Int)
        }
    }
}