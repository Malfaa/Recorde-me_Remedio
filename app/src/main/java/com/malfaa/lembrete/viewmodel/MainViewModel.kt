package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ItemRepository)  : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes = repository.recebeItem.asLiveData()// aqui

    companion object{
        val deletar = MutableLiveData(false)
        val alterar = MutableLiveData(false)
        val alarmeVar = MutableLiveData(false)
    }


    fun deletarLembrete(item: ItemEntidade){
        uiScope.launch {
            repository._deletarLembrete(item)
        }
    }
}