package com.malfaa.lembrete.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.fragment.AdicionarFragment
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.fragment.MainFragment.Companion.newInstance
import com.malfaa.lembrete.room.LDao
import java.util.*

class MainViewModel(dao: LDao)  : ViewModel() {

//    val job = Job()
//    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes = dao.recebeInfos()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    fun alarme() {
        alarmMgr = newInstance().context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(newInstance().context, MainFragment::class.java).let { intent ->
            PendingIntent.getBroadcast(newInstance().context, 0, intent, 0)
        }

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
        }

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 20,
            alarmIntent
        )
    }
}