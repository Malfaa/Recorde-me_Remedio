package com.malfaa.recorde_me_remedio

import android.annotation.SuppressLint
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

@SuppressLint("SetTextI18n")
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

@SuppressLint("SetTextI18n")
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

@BindingAdapter("notaValue")
fun notaValue(view:View, nota:String){
    if (nota.isEmpty()){
        view.visibility = View.GONE
    }
}

@BindingAdapter("clicks")
fun listenClicks(spinner: AppCompatSpinner, result: Int) {// FIXME: erro aqui
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            parent?.getItemAtPosition(result) as Int
        }
    }
}