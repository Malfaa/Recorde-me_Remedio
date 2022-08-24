package com.malfaa.recorde_me_remedio.remedio.alterar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class AlterarViewModel(private val repositorio: Repository): ViewModel() {

    val checkBox = MutableLiveData(false)

    val item = MutableLiveData<Remedio>()

    private val _navegarDeVolta = MutableLiveData(false)
    val navegarDeVolta : LiveData<Boolean>
        get() = _navegarDeVolta


    fun alterarRemedio(item: Remedio){
        viewModelScope.launch {
            repositorio.alterarRemedio(item)
        }
    }

    fun navegarDeVoltaFeito(){
        _navegarDeVolta.value = false
    }

}