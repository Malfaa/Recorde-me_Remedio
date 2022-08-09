package com.malfaa.recorde_me_remedio.remedio.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repositorio: Repository) : ViewModel() {

    val listaRemedio = repositorio.recebeItem

    //Quando o item desejado para Alterar for clicado, o id será passado para cá
    private val _recebeRemedio = MutableLiveData<Remedio>()
    val recebeRemedio : LiveData<Remedio>
        get() = _recebeRemedio

    fun deletarRemedio(item: Remedio) {
        viewModelScope.launch {
            repositorio.deletarLembrete(item)
        }
    }

}