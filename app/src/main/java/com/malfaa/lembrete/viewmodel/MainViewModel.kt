package com.malfaa.lembrete.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.room.LDao
import java.util.*

class MainViewModel(dao: LDao)  : ViewModel() {

//    val job = Job()
//    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes = dao.recebeInfos()


}