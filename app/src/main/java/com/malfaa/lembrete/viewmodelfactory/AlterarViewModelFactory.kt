package com.malfaa.lembrete.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodel.AlterarViewModel

class AlterarViewModelFactory (private val repository: ItemRepository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlterarViewModel::class.java)) {
            return AlterarViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}