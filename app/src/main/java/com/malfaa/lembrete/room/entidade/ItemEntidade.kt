package com.malfaa.lembrete.room.entidade

import android.os.Parcelable
import android.widget.AdapterView
import androidx.room.ColumnInfo
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
    var dataFinal: String,
    var hora: Int,
    var data: Int,
    var nota: String,
    @ColumnInfo(name = "custom_hora")
    var verificaHoraCustom: Boolean,
    @ColumnInfo(name = "custom_data")
    var verificaDataCustom: Boolean
): Parcelable
