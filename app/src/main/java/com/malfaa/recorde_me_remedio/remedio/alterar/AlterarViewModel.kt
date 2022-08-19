package com.malfaa.recorde_me_remedio.remedio.alterar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class AlterarViewModel(private val repositorio: Repository): ViewModel() {

    fun alterarRemedio(item: Remedio){
        viewModelScope.launch {
            repositorio.alterarRemedio(item)
        }
    }


}