package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.os.Build
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.recorde_me_remedio.horaFormato
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.minutoFormato
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class AdicionarViewModel(private val repositorio: Repository, private val frag: FragmentManager): ViewModel(){

    companion object{
        var horaInicial : String = ""
        var minutoInicial : String = ""
        lateinit var diaFinal : String
    }
    private val _diaFinal = MutableLiveData(false)
    val diaFinal : LiveData<Boolean>
        get() = _diaFinal

    fun diaFinalRetornaEstado(){
        _diaFinal.postValue(false)
    }

    private val _navegarDeVolta = MutableLiveData(false)
    val navegarDeVolta : LiveData<Boolean>
        get() = _navegarDeVolta

    fun adicionarRemedio(item: Remedio){
        _diaFinal.postValue(true)
        viewModelScope.launch {
            repositorio.adicionandoLembrete(item)
            _navegarDeVolta.value = true
        }
    }

    //Chama o picker e coloca o valor no textview horarioInicial
    fun picker():String{
        var horaFinal = ""
        val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
                .setMinute(LocalDateTime.now().minute).build()
        }else{
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Hora em que iniciará:").setHour(horaFormato(Date().time))
                .setMinute(minutoFormato(Date().time)).build()
        }

        picker.show(frag, "remedio")

        picker.addOnPositiveButtonClickListener{
            horaInicial = String.format("%02d", picker.hour)
            minutoInicial = String.format("%02d", picker.minute)
            horaFinal = "$horaInicial:$minutoInicial"

        }

        return horaFinal
    }



}