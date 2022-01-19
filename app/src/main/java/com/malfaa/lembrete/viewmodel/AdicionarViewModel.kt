package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.*

class AdicionarViewModel(private val dao: LDao)  : ViewModel() {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun adicionandoLembrete(item: ItemEntidade){
        uiScope.launch {
            dao.adicionaLembrete(_adicionandoLembrete(item))
        }
    }
    suspend fun _adicionandoLembrete(item: ItemEntidade): ItemEntidade {
        return withContext(Dispatchers.IO) {
            val valor = item
            valor
        }
    }

}