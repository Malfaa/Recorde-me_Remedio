package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
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