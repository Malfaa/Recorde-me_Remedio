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

    fun adicionarRemedio(item: Remedio){
        _diaFinal.value = true
        viewModelScope.launch {
            repositorio.adicionandoRemedio(item)
            _navegarDeVolta.value = true
        }
    }

}

// TODO: Abaixo do Período, colocar uma caixa de marcação (checkbox) que deixará o próprio período 
//  "infinito"(todos os dias) até remove-lo. Ele deixará não editável o EditText
// TODO: Alterar todos os layouts p/ a nova arquitetura