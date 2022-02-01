package com.malfaa.lembrete.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.*
import java.util.*

class MainViewModel(val ldao: LDao)  : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes = ldao.recebeInfos()

    companion object{
        val deletar = MutableLiveData(false)
        val alterar = MutableLiveData(false)
        val alarmeVar = MutableLiveData(false)
    }


    fun deletarLembrete(item: ItemEntidade){
        uiScope.launch {
            ldao.deletarLembrete(item)
        }
    }
}