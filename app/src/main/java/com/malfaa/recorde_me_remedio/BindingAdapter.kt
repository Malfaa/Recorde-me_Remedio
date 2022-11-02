package com.malfaa.recorde_me_remedio

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.malfaa.recorde_me_remedio.local.Remedio
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("notaValue")
fun notaValue(view:View, nota:String?){
    if (nota.isNullOrEmpty()){
        view.visibility = View.GONE
    }
}

//@BindingAdapter("horario")
//fun horario(textView: TextView, item: Remedio){
//    textView.text = String.format("%02d:%02d", horaParaAlarme(item.horaComeco.time), minutoParaAlarme(item.horaComeco.time)) //colocar o 00
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("todososdias")
fun todososdias(textView: TextView, item: Boolean){
    if(item) textView.text = "Todos os Dias"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("alterarAnteriormente")
fun alterarAnteriormente(textView: TextView, item: Remedio){
    if (item.todosOsDias){
        textView.text = "Todos os Dias"
    }else{
        textView.text = String.format("%d dia(s)", item.periodoDias)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("miliParaHoraMinuto")
fun miliParaHoraMinuto(textView: TextView, tempo: Long) {
    val date = Date(tempo)
    val formatter = SimpleDateFormat("HH:mm")

    textView.text = formatter.format(date)
}