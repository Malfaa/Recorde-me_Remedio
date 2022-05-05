package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.*

class AdicionarViewModel(private val repository: ItemRepository)  : ViewModel() {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val horarioFinal = MutableLiveData<String>(null)

    fun adicionandoLembrete(item: ItemEntidade){
        uiScope.launch {
            repository._adicionandoLembrete(item)
        }
    }



}