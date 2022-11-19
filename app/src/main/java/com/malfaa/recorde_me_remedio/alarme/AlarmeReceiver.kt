package com.malfaa.recorde_me_remedio.alarme

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.malfaa.recorde_me_remedio.DespertadorActivity
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_ACTION
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.notificacao.sendNotification


class AlarmeReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (intent.action == INTENT_ACTION) {
            val item = intent.getBundleExtra(INTENT_BUNDLE)?.getParcelable<Remedio>(INTENT_BUNDLE)

            if(item?.despertador == true){
                val novoIntent = Intent (context, DespertadorActivity().ativaDespertador(context, item)::class.java)
                startActivity(context, novoIntent,null) //passa o activity e o service fica dentro dele fixme testar
             }//else{

            notificationManager.sendNotification(context, item!!)
            AlarmeService().removerAlarme(context, item)
            AlarmeService().adicionarAlarme(context, item, item.horaEmHora)
            //colocar aqui um if que verifica se foi marcado o checkbox, que daí ele ativa ou não o despertador
            // que chamará uma tela que toca e vibra falando o nome do remédio. Tocar na tela cancela
            //pergunta, como lançar um fragment com a tela bloqueada?
        }
    }
}
//https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/