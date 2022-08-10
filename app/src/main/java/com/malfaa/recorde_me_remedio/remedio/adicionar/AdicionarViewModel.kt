package com.malfaa.recorde_me_remedio.remedio.adicionar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class AdicionarViewModel(private val repositorio: Repository): ViewModel(){

    fun adicionarRemedio(item: Remedio){
        viewModelScope.launch {
            repositorio.adicionandoLembrete(item)
        }
    }
}