package com.malfaa.recorde_me_remedio

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("notaValue")
fun notaValue(view:View, nota:String?){
    if (nota.isNullOrEmpty()){
        view.visibility = View.GONE
    }
}

//object BindingUtils {
//
//    private const val ON_LONG_CLICK = "android:onLongClick"
//
//    @JvmStatic
//    @BindingAdapter(ON_LONG_CLICK)
//    fun setOnLongClickListener(
//        view: View,
//        longClickListener: (Remedio) -> Unit
//    ) {
//        view.setOnLongClickListener {
//            fun onLongClick(remedio: Remedio)=longClickListener(remedio)
//            true
//        }
//    }
//}