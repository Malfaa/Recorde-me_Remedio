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

@SuppressLint("SetTextI18n")
@BindingAdapter("todososdias")
fun todososdias(textView: TextView, item: Remedio){
    when(item.linguagem){
        "português" -> if(item.todosOsDias) textView.text = "Todos os Dias"
        else -> if(item.todosOsDias) textView.text = "Every day"
    }

}

@SuppressLint("SetTextI18n")
@BindingAdapter("alterarAnteriormente")
fun alterarAnteriormente(textView: TextView, item: Remedio){
    when(item.linguagem) {
        "português" ->
            if (item.todosOsDias){
                textView.text = "Todos os Dias"
            }else{
                textView.text = String.format("%d dia(s)", item.periodoDias)
            }
        else ->
            if (item.todosOsDias){
                textView.text = "Every day"
            }else{
                textView.text = String.format("%d day(s)", item.periodoDias)
            }
    }

}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("miliParaHoraMinuto")
fun miliParaHoraMinuto(textView: TextView, item: Remedio) {
    when(item.linguagem){
        "português" ->{
            val date = Date(item.horaComecoEmMillis)
            val formatter = SimpleDateFormat("HH:mm")

            textView.text = formatter.format(date)
        }
        else->{
            val date = Date(item.horaComecoEmMillis)
            val formatter = SimpleDateFormat("hh:mm a")

            textView.text = formatter.format(date)
        }
    }
}