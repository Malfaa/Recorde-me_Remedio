package com.malfaa.recorde_me_remedio.room

import androidx.room.*
import com.malfaa.recorde_me_remedio.room.entidade.ItemEntidade
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