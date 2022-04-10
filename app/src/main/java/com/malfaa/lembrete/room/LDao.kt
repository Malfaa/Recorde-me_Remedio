package com.malfaa.lembrete.room

import androidx.room.*
import com.malfaa.lembrete.room.entidade.ItemEntidade
import kotlinx.coroutines.flow.Flow

@Dao
interface LDao {
    @Query("SELECT * FROM item")
    fun recebeInfos(): Flow<List<ItemEntidade>>

    @Insert
    suspend fun adicionaLembrete(item: ItemEntidade)

    @Update
    suspend fun atualizaLembrete(item: ItemEntidade)

    @Delete
    suspend fun deletarLembrete(item: ItemEntidade)
}