package com.malfaa.recorde_me_remedio

import android.app.KeyguardManager
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.utils.Constantes.INTENT_BUNDLE
import com.malfaa.recorde_me_remedio.utils.Horario


class DespertadorActivity : AppCompatActivity() {

    private lateinit var som: MediaPlayer
    private lateinit var vib: Vibrator

    private val dispensar: ImageView
        get() = findViewById(R.id.dispensar)

    private val imagem: ImageView
        get() = findViewById(R.id.imagem)

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

        if(Build.VERSION.SDK_INT < 22){
            imagem.setImageResource(R.mipmap.ic_clockv2)
        }

        vibrar()

        alarme()

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
        hora.text = Horario.miliParaHoraMinuto(item, System.currentTimeMillis())

        dispensar.setOnLongClickListener{
            som.stop()
            vib.cancel()
            finish()
            true
        }

    }

    private fun alarme(){
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        som = MediaPlayer.create(this, alarmSound)

        som.start()
        som.setVolume(0.6F, 0.6F)

        som.isLooping = true
    }

    private fun vibrar(){
        val atraso = 0
        val vibra = 1000
        val dorme = 1000

        val vibratePattern = longArrayOf(atraso.toLong(), vibra.toLong(), dorme.toLong())

        vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(
                VibrationEffect.createWaveform(vibratePattern, 0),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
        } else {
            @Suppress("DEPRECATION")
            vib.vibrate(vibratePattern, 0)
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
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }
}