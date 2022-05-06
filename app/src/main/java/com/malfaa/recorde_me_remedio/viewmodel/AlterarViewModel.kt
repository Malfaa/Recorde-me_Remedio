package com.malfaa.recorde_me_remedio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.recorde_me_remedio.repository.ItemRepository
import com.malfaa.recorde_me_remedio.room.entidade.ItemEntidade
import kotlinx.coroutines.*

class AlterarViewModel(private val repository: ItemRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val horarioFinal = MutableLiveData<String>(null)

    fun alterarLembrete(item: ItemEntidade){
        uiScope.launch {
            repository._alterarLembrete(item)
        }
    }

}