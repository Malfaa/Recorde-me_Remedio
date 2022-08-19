package com.malfaa.recorde_me_remedio.remedio.adicionar

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.recorde_me_remedio.repository.Repository

class AdicionarViewModelFactory(private val repositorio: Repository):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdicionarViewModel::class.java)){
            return AdicionarViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}