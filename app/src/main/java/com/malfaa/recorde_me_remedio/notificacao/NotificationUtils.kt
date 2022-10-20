package com.malfaa.recorde_me_remedio.notificacao

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.malfaa.recorde_me_remedio.MainActivity
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.local.Remedio

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(applicationContext: Context, remedio: Remedio) {
    // Create the content intent for the notification, which launches
    // this activity
    val intent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        remedio.requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.remedio_notification_channel_id))
        .setSmallIcon(R.drawable.ic_clock)
        .setContentTitle(remedio.remedio)
        .setContentText(remedio.nota)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(remedio.requestCode, builder.build())
}

fun NotificationManager.cancelNotification(requestCode:Int){
    cancel(requestCode)
}

//Cancels all notifications.
fun NotificationManager.cancelNotifications() {
    cancelAll()
}