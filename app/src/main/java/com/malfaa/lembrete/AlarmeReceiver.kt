package com.malfaa.lembrete

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.nota
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.remedio
import com.malfaa.lembrete.fragment.MainFragment
import com.malfaa.lembrete.room.entidade.ItemEntidade
//mudei aqui abaixo
class AlarmeReceiver(val remedio:String, val nota:String) : BroadcastReceiver(){

    //private lateinit var alarmIntent: PendingIntent

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        try{
//        alarmIntent = Intent(
//            context, MainFragment::class.java).let {
//            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            PendingIntent.getBroadcast(context, 0, intent!!, 0)
//        }
        val alarmIntent = Intent(context, MainFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
// FIXME: mudei linha 32 / usar o id do lembrete pra adicionar o alarme
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,alarmIntent,0)  //Intent.FLAG_ACTIVITY_NEW_TASK

        val builder = NotificationCompat.Builder(context!!, "notificacao")
            // FIXME: talvez pegar direto do banco de dados os valores, pq aqui quando destruído o
            //  app, as variáveis são apagadas, resultando em retornar null
            .setSmallIcon(R.drawable.ic_clock)
            //mudei aqui abaixo
            .setContentTitle(remedio/*remedio.value.toString()*/) //aqui
            .setContentText(nota/*nota.value. toString()*/) // aqui
            //.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)//alarmIntent

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())

        Toast.makeText(MainFragment().requireContext(), "Alarme disparado", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Log.d(context?.toString(), e.toString())
        }
    }
}
