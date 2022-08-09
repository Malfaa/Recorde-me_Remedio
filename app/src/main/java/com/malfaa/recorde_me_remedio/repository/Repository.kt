package com.malfaa.recorde_me_remedio.repository

import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: RemedioDatabase) : IRepository {

    val recebeItem = database.dao.getRemedios()

    override suspend fun adicionandoLembrete(item: Remedio){
        return withContext(Dispatchers.IO) {
            database.dao.adicionarRemedio(item)
        }
    }

    override suspend fun alterarLembrete(item:Remedio){
        return withContext(Dispatchers.IO){
            database.dao.atualizarRemedio(item)
        }
    }

    override suspend fun deletarLembrete(item:Remedio){
        return withContext(Dispatchers.IO){
            database.dao.deletarRemedio(item)
        }
    }
}