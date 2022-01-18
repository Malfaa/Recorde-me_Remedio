package com.malfaa.lembrete.room.entidade

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "item")
data class ItemEntidade(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var remedio: String,
    var hora: String,
    var data: Date,
    var nota: String
)
