package com.malfaa.recorde_me_remedio.repository

import com.malfaa.recorde_me_remedio.local.Remedio

interface IRepository {

    suspend fun adicionandoLembrete(item: Remedio)

    suspend fun alterarLembrete(item: Remedio)

    suspend fun deletarLembrete(item: Remedio)
}