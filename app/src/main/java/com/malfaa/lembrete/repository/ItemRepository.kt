package com.malfaa.lembrete.repository

import com.malfaa.lembrete.room.LDao
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ItemRepository(private val itemDao: LDao) {

    val recebeItem: Flow<List<ItemEntidade>> = itemDao.recebeInfos()

    suspend fun _adicionandoLembrete(item: ItemEntidade){
        return withContext(Dispatchers.IO) {
            itemDao.adicionaLembrete(item)
        }
    }

    suspend fun _alterarLembrete(item:ItemEntidade){
        return withContext(Dispatchers.IO){
            itemDao.atualizaLembrete(item)
        }
    }

    suspend fun _deletarLembrete(item:ItemEntidade){
        return withContext(Dispatchers.IO){
            itemDao.deletarLembrete(item)
        }
    }

}
//https://digital-solutions.consulting/blog/repository-in-androids-mvvm-architecture/
//https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06