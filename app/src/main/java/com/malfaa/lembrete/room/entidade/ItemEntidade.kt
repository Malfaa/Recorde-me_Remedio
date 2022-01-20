package com.malfaa.lembrete.room.entidade

import android.os.Parcelable
import android.widget.AdapterView
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date

@Parcelize
@Entity(tableName = "item")
data class ItemEntidade(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var remedio: String,
    var horaInicial: String,
    var hora: Long,
    var data: Long,
    var nota: String
): Parcelable
