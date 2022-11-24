package com.malfaa.recorde_me_remedio

import android.app.KeyguardManager
import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE
import com.malfaa.recorde_me_remedio.utils.miliParaHoraMinuto

class DespertadorActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    private val tela: LinearLayoutCompat
        get() = findViewById(R.id.tela)

    private val remedio: TextView
        get() = findViewById(R.id.remedioDespertador)
    private val nota: TextView
        get() = findViewById(R.id.notaDespertador)

    private val hora: TextView
        get() = findViewById(R.id.horaDespertador)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_despertador)
        showWhenLockedAndTurnScreenOn()

        val bundleRemedio = intent.getBundleExtra(INTENT_BUNDLE)
            ?.getParcelable<Remedio>(INTENT_BUNDLE)

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(this, alarmSound)

        mediaPlayer.start()

        mediaPlayer.isLooping = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE) )
        }else{
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }

        ativaDespertador(bundleRemedio!!)
    }

    private fun ativaDespertador(item: Remedio){
        //manipula o layout
        remedio.text = item.remedio
        if(item.nota!!.isNotEmpty()){
            nota.visibility = View.VISIBLE
            nota.text = item.nota
        }else{
            nota.visibility = View.GONE
        }
        hora.text = miliParaHoraMinuto(System.currentTimeMillis())

        tela.setOnClickListener{
            mediaPlayer.stop()
            vibrator.cancel()
            finish()
        }

    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager: KeyguardManager= getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        }
        else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }
}