package com.malfaa.lembrete.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.malfaa.lembrete.room.entidade.ItemEntidade

@Dao
interface LDao {
    @Query("SELECT * FROM item")
    fun recebeInfos(): LiveData<List<ItemEntidade>>

    @Insert
    suspend fun adicionaLembrete(item: ItemEntidade)

    @Update
    suspend fun atualizaLembrete(item: ItemEntidade)

    @Delete
    suspend fun deletarLembrete(item: ItemEntidade)
}