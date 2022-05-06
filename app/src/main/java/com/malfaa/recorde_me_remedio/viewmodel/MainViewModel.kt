package com.malfaa.recorde_me_remedio.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.malfaa.recorde_me_remedio.conversorPosEmMinutos
import com.malfaa.recorde_me_remedio.fragment.MainFragment.Companion.viewlifeCycleOwner
import com.malfaa.recorde_me_remedio.receiver.AlarmReceiver
import com.malfaa.recorde_me_remedio.repository.ItemRepository
import com.malfaa.recorde_me_remedio.room.entidade.ItemEntidade
import kotlinx.coroutines.*
import java.util.*

class MainViewModel(private val repository: ItemRepository)  : ViewModel() {

    private val job     = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val listaLembretes    = repository.recebeItem.asLiveData()// aqui

    private lateinit var alarmMgr          : AlarmManager
    private lateinit var alarmPendingIntent: PendingIntent
    private lateinit var intent            : Intent

    companion object{
        val deletar   = MutableLiveData(false)
        val alterar   = MutableLiveData(false)
        val alarmeVar = MutableLiveData(false)
        val lembretes = mutableListOf<ItemEntidade>()

    }


    fun deletarLembrete(item: ItemEntidade){
        uiScope.launch {
            repository._deletarLembrete(item)
        }
    }

    private suspend fun listaLembrete(){
        return withContext(Dispatchers.Main){
            listaLembretes.observe(viewlifeCycleOwner){
                    lembrar ->
                if(lembrar != null){
                    for(itens in lembrar){//19:02
                        lembretes.add(itens)
                    }
                }
                lembretes.last()
            }
        }}

    @SuppressLint("UnspecifiedImmutableFlag")
    fun adicionarAlarmeItens(context: Context) {
        uiScope.launch {
            delay(1000)
            listaLembrete()

            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            intent = Intent(context, AlarmReceiver::class.java)

            alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                lembretes.last().requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, lembretes.last().horaInicial.take(2).toInt()/*horas[item.hora]*/)
                set(Calendar.MINUTE, lembretes.last().horaInicial.takeLast(2).toInt()/*minutos[item.hora]*/)
            }

            if (!lembretes.last().verificaHoraCustom) {
                alarmMgr.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    1000 * 60 * (60 * conversorPosEmMinutos(lembretes.last().hora)!!/*horario*/),
                    alarmPendingIntent
                )
            } else {
                alarmMgr.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    1000 * 60 * (60 * lembretes.last().hora.toLong())/*horario*/,
                    alarmPendingIntent
                )
            }
        }
    }
}