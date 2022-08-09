package com.malfaa.recorde_me_remedio.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.recorde_me_remedio.Constantes

@Dao
interface RemedioDao {
    @Query("SELECT * FROM ${Constantes.TABLE_NAME}")
    fun getRemedios(): LiveData<List<Remedio>>

    @Insert
    suspend fun adicionarRemedio(item: Remedio)

    @Update
    suspend fun atualizarRemedio(item: Remedio)

    @Delete
    suspend fun deletarRemedio(item: Remedio)
}