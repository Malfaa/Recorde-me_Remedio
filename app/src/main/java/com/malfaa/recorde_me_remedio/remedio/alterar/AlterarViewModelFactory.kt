package com.malfaa.recorde_me_remedio.remedio.alterar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.recorde_me_remedio.repository.Repository

class AlterarViewModelFactory(private val repositorio: Repository) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlterarViewModel::class.java)) {
            return AlterarViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}