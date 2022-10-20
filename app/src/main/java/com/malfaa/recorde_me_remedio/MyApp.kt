package com.malfaa.recorde_me_remedio

import android.app.Application
import android.os.Build
import androidx.work.*
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
import com.malfaa.recorde_me_remedio.remedio.alterar.AlterarViewModel
import com.malfaa.recorde_me_remedio.remedio.main.MainViewModel
import com.malfaa.recorde_me_remedio.repository.Repository
import com.malfaa.recorde_me_remedio.workmanager.VerificaDiaENotifica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        val meuModulo = module {

            single{RemedioDatabase.getInstance(this@MyApp)}
            single{Repository(get() as RemedioDatabase)}

            viewModel{ MainViewModel(get() as Repository) }
            viewModel{ AdicionarViewModel(get() as Repository) }
            viewModel{ AlterarViewModel(get() as Repository) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(meuModulo)
        }
        delayedInit()

    }
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(false)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<VerificaDiaENotifica>(
            1,
            TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            Constantes.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}