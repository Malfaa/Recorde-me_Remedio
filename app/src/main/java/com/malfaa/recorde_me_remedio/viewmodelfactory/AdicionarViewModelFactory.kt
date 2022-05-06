package com.malfaa.recorde_me_remedio.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.recorde_me_remedio.repository.ItemRepository
import com.malfaa.recorde_me_remedio.viewmodel.AdicionarViewModel

class AdicionarViewModelFactory(private val repository: ItemRepository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdicionarViewModel::class.java)){
            return AdicionarViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}