package com.malfaa.recorde_me_remedio

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("notaValue")
fun notaValue(view:View, nota:String?){
    if (nota.isNullOrEmpty()){
        view.visibility = View.GONE
    }
}

@BindingAdapter("mudaExemplo")
fun mudaExemplo(editText: EditText, textView: TextView){
    textView.text = String.format("%s em %s hora(s)", editText.text)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("todososdias")
fun todososdias(textView: TextView, item: Boolean){
    if(item) textView.text = "Todos os Dias"
}