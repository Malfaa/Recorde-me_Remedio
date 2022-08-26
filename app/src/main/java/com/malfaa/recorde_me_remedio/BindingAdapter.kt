package com.malfaa.recorde_me_remedio

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("notaValue")
fun notaValue(view:View, nota:String?){
    if (nota.isNullOrEmpty()){
        view.visibility = View.GONE
    }
}