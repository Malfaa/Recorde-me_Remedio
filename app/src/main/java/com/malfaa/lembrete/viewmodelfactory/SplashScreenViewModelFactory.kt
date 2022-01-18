package com.malfaa.lembrete.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodel.SplashScreenViewModel

class SplashScreenViewModelFactory (private val dao: LDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashScreenViewModel::class.java)){
            return SplashScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}