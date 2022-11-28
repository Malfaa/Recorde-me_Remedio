package com.malfaa.recorde_me_remedio.notificacao

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.malfaa.recorde_me_remedio.DespertadorActivity
import com.malfaa.recorde_me_remedio.MainActivity
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(applicationContext: Context, remedio: Remedio) {

    val intent: Intent = Intent(applicationContext, DespertadorActivity::class.java)
    .putExtra(INTENT_BUNDLE, bundleOf(INTENT_BUNDLE to remedio))

    val intentClickable = Intent(applicationContext, MainActivity::class.java)

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

    val clickingPendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getActivity(
            applicationContext,
            remedio.requestCode,
            intentClickable,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getActivity(
            applicationContext,
            remedio.requestCode,
            intentClickable,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.remedio_notification_channel_id))
        .setContentTitle(remedio.remedio)
        .setContentText(remedio.nota)
        .setContentIntent(clickingPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setFullScreenIntent(pendingIntent,true).apply {
            if(Build.VERSION.SDK_INT >= 23){
                this.setSmallIcon(R.drawable.ic_clock)
            }else{
                this.setSmallIcon(R.mipmap.ic_clockv2_foreground)
            }
        }

    notify(remedio.requestCode, builder.build())
}

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendLastDayNotification(applicationContext: Context) {

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