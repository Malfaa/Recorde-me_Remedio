package com.malfaa.recorde_me_remedio.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.recorde_me_remedio.room.LDao
import com.malfaa.recorde_me_remedio.viewmodel.SplashScreenViewModel

class SplashScreenViewModelFactory (private val dao: LDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashScreenViewModel::class.java)){
            return SplashScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}