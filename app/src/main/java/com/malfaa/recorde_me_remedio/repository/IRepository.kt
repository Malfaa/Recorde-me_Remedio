package com.malfaa.recorde_me_remedio.repository

import com.malfaa.recorde_me_remedio.local.Remedio

interface IRepository {

    suspend fun retornaUltimoDia(): List<String>

    suspend fun adicionandoRemedio(item: Remedio)

    suspend fun alterarRemedio(item: Remedio)

    suspend fun deletarRemedio(item: Remedio)
}