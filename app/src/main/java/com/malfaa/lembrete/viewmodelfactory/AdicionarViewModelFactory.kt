package com.malfaa.lembrete.viewmodelfactory

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.viewmodel.AdicionarViewModel

class AdicionarViewModelFactory(private val dao: LDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdicionarViewModel::class.java)){
            return AdicionarViewModel(dao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}