package com.malfaa.recorde_me_remedio.remedio.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repositorio: Repository) : ViewModel() {

    companion object{
        val remedioItem = MutableLiveData<Remedio>()
        val deletar = MutableLiveData(false)
    }

    val listaRemedio = repositorio.recebeItem

    fun deletarRemedio(item: Remedio) {
        viewModelScope.launch {
            repositorio.deletarRemedio(item)
        }
    }

    fun criandoCanalDeNotificacao(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nome = "recorde-meRemedio"
            val descricao = "Canal de notificacao p/ lembretes"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel(context.getString(R.string.remedio_notification_channel_id), nome, importancia).apply { description = descricao }
            val gerenciadorNotificacao =
                ContextCompat.getSystemService(context.applicationContext, NotificationManager::class.java)

            gerenciadorNotificacao?.createNotificationChannel(canal)
        }
    }
}