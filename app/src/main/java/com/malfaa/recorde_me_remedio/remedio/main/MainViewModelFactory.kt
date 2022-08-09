package com.malfaa.recorde_me_remedio.remedio.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.recorde_me_remedio.repository.Repository

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory  {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}