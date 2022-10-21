package com.malfaa.recorde_me_remedio.notificacao

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.malfaa.recorde_me_remedio.MainActivity
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.local.Remedio

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(applicationContext: Context, remedio: Remedio) {
    // Create the content intent for the notification, which launches
    // this activity
    val intent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getActivity(
            applicationContext,
            remedio.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getActivity(
            applicationContext,
            remedio.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

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

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendLastDayNotification(applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    val intent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT)
    }

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.remedio_notification_channel_id))
        .setSmallIcon(R.drawable.ic_clock)
        .setContentTitle(applicationContext.resources.getString(R.string.ultimo_dia_titulo))
        .setContentText(applicationContext.resources.getString(R.string.ultimo_dia_nota))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(0, builder.build())
}

fun NotificationManager.cancelNotification(requestCode:Int){
    cancel(requestCode)
}

//Cancels all notifications.
fun NotificationManager.cancelNotifications() {
    cancelAll()
}