package com.malfaa.lembrete.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.*

class MainViewModel(private val dao: LDao)  : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes = dao.recebeInfos()


}