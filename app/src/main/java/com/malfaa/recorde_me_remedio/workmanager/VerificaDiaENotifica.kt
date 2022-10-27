package com.malfaa.recorde_me_remedio.workmanager

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.malfaa.recorde_me_remedio.utils.calendarioParaData
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.notificacao.sendLastDayNotification
import com.malfaa.recorde_me_remedio.repository.Repository
import java.util.*

class VerificaDiaENotifica (private val context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = RemedioDatabase.getInstance(applicationContext)
        val repository = Repository(database)
        val calendario = Calendar.getInstance()
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        return try {
            val list = repository.retornaUltimoDia()
            for(i in list){
                if(calendarioParaData(calendario.time) == i){
                    notificationManager.sendLastDayNotification(context)
                }
            }

            Result.success()
        }catch (e:Exception){
            Result.retry()
        }
    }}