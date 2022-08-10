package com.malfaa.recorde_me_remedio.remedio.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repositorio: Repository) : ViewModel() {

    val listaRemedio = repositorio.recebeItem

    fun deletarRemedio(item: Remedio) {
        viewModelScope.launch {
            repositorio.deletarLembrete(item)
        }
    }

}