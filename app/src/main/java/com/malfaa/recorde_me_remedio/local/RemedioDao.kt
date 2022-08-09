package com.malfaa.recorde_me_remedio.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.recorde_me_remedio.Constantes

@Dao
interface RemedioDao {
    @Query("SELECT * FROM ${Constantes.TABLE_NAME}")
    fun recebeInfos(): LiveData<List<Remedio>>

    @Insert
    suspend fun adicionarLembrete(item: Remedio)

    @Update
    suspend fun atualizarLembrete(item: Remedio)

    @Delete
    suspend fun deletarLembrete(item: Remedio)
}