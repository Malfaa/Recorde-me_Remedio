package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.*
import com.malfaa.lembrete.databinding.AdicionarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodelfactory.AdicionarViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AdicionarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun adicionarInstance() = AdicionarFragment()

        var horaEscolhida by Delegates.notNull<Long>()
        var minutoEscolhido by Delegates.notNull<Long>()
        var horarioFinal: String = "" // FIXME: 20/01/2022 arrumar aqui
    }

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    private lateinit var binding: AdicionarFragmentBinding
    private lateinit var viewModel: AdicionarViewModel
    private lateinit var viewModelFactory: AdicionarViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.adicionar_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Novo Lembrete"

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = AdicionarViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AdicionarViewModel::class.java]

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.list_datas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dataSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.list_horario,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.horaSpinner.adapter = adapter
        }

        binding.horaInicialValue.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val text = SimpleDateFormat("HH:mm").format(cal.time)
                horaEscolhida = hour.toLong()
                minutoEscolhido = minute.toLong()
                Log.d("Valores Horarios", "$horaEscolhida e $minutoEscolhido")
                horarioFinal = text
            }
            TimePickerDialog(this.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            Log.d("Valor Relógio", horarioFinal)
        }

        binding.adicionar.setOnClickListener {
            try {
                Log.d("Spinner", binding.horaSpinner.selectedItem.toString())
                alarme(
                    horaEscolhida,
                    minutoEscolhido,
                    conversorStringEmMinutos(binding.horaSpinner.selectedItem.toString())
                )
                viewModel.adicionandoLembrete(
                    ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString(),
                        horarioFinal,
                        binding.horaSpinner.selectedItem.toString(),
                        binding.dataSpinner.selectedItem.toString(),
                        binding.notaText.text.toString()
                    )
                )
                this.findNavController()
                    .navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
            }catch (e: Exception){
                Log.d("error", e.toString())
            }
        }

        binding.retornar.setOnClickListener {
            this.findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
    }
    //binding.dataSpinner?.onItemSelectedListener = this
    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun alarme(hora: Long, minutos: Long, horario: Long) { //mainviewmodel    horario é em millis, logo, valor tem que ser long
        alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(
            requireContext(), MainFragment::class.java).let { intent ->
            PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        }

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hora.toInt())
            set(Calendar.MINUTE, minutos.toInt())
        }

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * horario,  //60000 * (60 * 4) = 60000 * '240' = 144000000
            alarmIntent
        )
    }

}// TODO: 19/01/2022 arrumar large_adicionar
// FIXME: 20/01/2022 alarme num funfa