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

    private val _visibilidadeHora = MutableLiveData(false)
    val visibilidadeHora : LiveData<Boolean>
        get() = _visibilidadeHora

    private val _visibilidadeData = MutableLiveData(false)
    val visibilidadeData : LiveData<Boolean>
        get() = _visibilidadeData


    fun adicionarRemedio(item: Remedio){
        _diaFinal.value = true
        viewModelScope.launch {
            repositorio.adicionandoRemedio(item)
            _navegarDeVolta.value = true
        }
    }

    fun visibilidadeHora(){
        if (visibilidadeHora.value == true){
            _visibilidadeHora.postValue(false)
        }else{
            _visibilidadeHora.postValue(true)
        }

    }

    fun visibilidadeData(){
        if (visibilidadeData.value == true){
            _visibilidadeData.postValue(false)
        }else{
            _visibilidadeData.postValue(true)
        }
    }
}