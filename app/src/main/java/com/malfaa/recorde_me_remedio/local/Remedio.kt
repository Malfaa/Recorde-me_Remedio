package com.malfaa.recorde_me_remedio.local

import android.os.Parcelable
import androidx.room.*
import com.malfaa.recorde_me_remedio.Constantes
import com.malfaa.recorde_me_remedio.diaAtual
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
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
    @ColumnInfo(name = "hora_comeco")
    var horaComeco: String,
    var nota: String?,
//    var requestCode: Int
): Parcelable{
    @IgnoredOnParcel
    @ColumnInfo(name = "primeiro_dia")
    lateinit var primeiroDia: String

    @IgnoredOnParcel
    @ColumnInfo(name = "ultimo_dia")
    lateinit var ultimoDia: String

}