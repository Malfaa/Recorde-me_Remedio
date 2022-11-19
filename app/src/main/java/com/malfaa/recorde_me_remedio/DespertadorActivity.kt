package com.malfaa.recorde_me_remedio

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.utils.miliParaHoraMinuto

class DespertadorActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private val tela = findViewById<LinearLayout>(R.id.tela)
    private val remedio = findViewById<TextView>(R.id.remedioDespertador)
    private val hora = findViewById<TextView>(R.id.horaDespertador)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_despertador)

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mediaPlayer = MediaPlayer.create(this, alarmSound)

        mediaPlayer.isLooping = true


    }

    fun ativaDespertador(context: Context, item: Remedio){
        //manipula o layout

        mediaPlayer.start()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE) )
        }else{
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }

        remedio.text = item.remedio
        hora.text = miliParaHoraMinuto(System.currentTimeMillis())

        tela.setOnClickListener{
            mediaPlayer.stop()
            vibrator.cancel()
            finish()
        }


    }


}