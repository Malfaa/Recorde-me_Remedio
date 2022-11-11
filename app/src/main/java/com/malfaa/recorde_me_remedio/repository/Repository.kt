package com.malfaa.recorde_me_remedio.repository

import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: RemedioDatabase) : IRepository {

    val recebeItem = database.dao.getRemedios()

    override suspend fun remediosReboot(): List<Remedio> {
        return withContext(Dispatchers.Main) {
            database.dao.getRemedioReboot()
        }
    }

    override suspend fun retornaUltimoDia(): List<String> {
        return withContext(Dispatchers.IO) {
            database.dao.getUltimoDia()
        }
    }

    override suspend fun adicionandoRemedio(item: Remedio){
        return withContext(Dispatchers.IO) {
            database.dao.adicionarRemedio(item)
        }
    }

    override suspend fun alterarRemedio(item:Remedio){
        return withContext(Dispatchers.IO){
            database.dao.atualizarRemedio(item)
        }
    }

    override suspend fun deletarRemedio(item:Remedio){
        return withContext(Dispatchers.IO){
            database.dao.deletarRemedio(item)
        }
    }
}