package com.malfaa.recorde_me_remedio.local

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "remedio")
data class Remedio(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var remedio: String,
    @ColumnInfo(name = "hora_inicial")
    var horaInicial: String,
    @ColumnInfo(name = "data_conjunto")
    var dataConjunto: String,
    var hora: Int,
    var data: Int,
    var nota: String,
    @ColumnInfo(name = "custom_hora")
    var verificaHoraCustom: Boolean,
    @ColumnInfo(name = "custom_data")
    var verificaDataCustom: Boolean,
    var requestCode: Int
): Parcelable
