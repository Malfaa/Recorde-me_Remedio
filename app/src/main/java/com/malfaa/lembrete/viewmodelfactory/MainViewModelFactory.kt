package com.malfaa.lembrete.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.viewmodel.MainViewModel

class MainViewModelFactory(private val dao: LDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}