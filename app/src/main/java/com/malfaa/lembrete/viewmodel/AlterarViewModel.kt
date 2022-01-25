package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.*

class AlterarViewModel(private val dao: LDao) : ViewModel() {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val horarioFinal = MutableLiveData<String>(null)

    fun alterarLembrete(item: ItemEntidade){
        uiScope.launch {
            _alterarLembrete(item)
        }
    }

    private suspend fun _alterarLembrete(item: ItemEntidade){
        withContext(Dispatchers.IO){
            val valor = dao.atualizaLembrete(item)
            return@withContext valor
        }
    }
}