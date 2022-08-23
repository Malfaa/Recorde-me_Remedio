package com.malfaa.recorde_me_remedio.remedio.adicionar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class AdicionarViewModel(private val repositorio: Repository): ViewModel(){

    companion object{
        var horaInicial : String = ""
        var minutoInicial : String = ""
    }

    val checkBox = MutableLiveData(false)

    private val _navegarDeVolta = MutableLiveData(false)
    val navegarDeVolta : LiveData<Boolean>
        get() = _navegarDeVolta

    fun adicionarRemedio(item:Remedio){
        viewModelScope.launch {
            repositorio.adicionandoRemedio(item)
            _navegarDeVolta.value = true
        }
    }

    fun navegarDeVoltaFeito(){
        _navegarDeVolta.value = false
    }
}