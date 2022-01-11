package com.malfaa.lembrete.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.malfaa.lembrete.room.entidade.ItemEntidade

@Dao
interface Dao {
    @Query("SELECT * FROM item")
    fun recebeInfos(): LiveData<List<ItemEntidade>>

    @Insert
    suspend fun adicionaLembrete(nota: ItemEntidade)

    @Update
    suspend fun atualizaLembrete(nota: ItemEntidade)

    @Delete
    suspend fun deletarLembrete(nota: ItemEntidade)
}