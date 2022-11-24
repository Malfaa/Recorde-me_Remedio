package com.malfaa.recorde_me_remedio.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfaa.recorde_me_remedio.utils.Constantes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constantes.TABLE_NAME)
data class Remedio(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var remedio: String,
    @ColumnInfo(name = "hora_em_hora")
    var horaEmHora: Int,
    @ColumnInfo(name = "periodo_em_dias")
    var periodoDias: Int,
    @ColumnInfo(name = "hora_comeco_em_millis")
    var horaComecoEmMillis: Long,
    var nota: String?,
    @ColumnInfo(name = "todos_Dias")
    var todosOsDias: Boolean = false,
    var requestCode: Int
): Parcelable{
    @IgnoredOnParcel
    @ColumnInfo(name = "primeiro_dia")
    lateinit var primeiroDia: String

    @IgnoredOnParcel
    @ColumnInfo(name = "ultimo_dia")
    lateinit var ultimoDia: String
}