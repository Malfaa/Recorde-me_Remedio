package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import com.malfaa.lembrete.R
import com.malfaa.lembrete.alarme
import com.malfaa.lembrete.conversorStringEmMinutos
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
        lateinit var horarioFinal: String
    }

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

        // FIXME: 19/01/2022 estranho aqui
        binding.horaInicialValue.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val text = SimpleDateFormat("HH:mm").format(cal.time)
                //text.toLong()
                horaEscolhida = hour.toLong()
                minutoEscolhido = minute.toLong()
                horarioFinal = text//"$horaEscolhida:$minutoEscolhido"
            }
            TimePickerDialog(this.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            //binding.horaInicialValue.text = horarioFinal
        } // FIXME: 20/01/2022 talvez trocar o botão por outra tag(?)


        binding.adicionar.setOnClickListener {
            try {
                viewModel.adicionandoLembrete(ItemEntidade(
                    0, binding.campoRemedio.toString(),
                    binding.horaInicialValue.toString(),
                    binding.horaSpinner.onItemSelectedListener.toString().toLong(),
                    binding.dataSpinner.onItemSelectedListener.toString().toLong(),
                    binding.notaText.toString())
                )

                alarme(horaEscolhida, minutoEscolhido, conversorStringEmMinutos(horarioFinal.toInt()))//(binding.horaSpinner.onItemSelectedListener.toString().toInt()))

            }catch (e:Exception){
                Log.d("error", e.toString())
            }
        }

        binding.retornar.setOnClickListener {
            this.findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }

    }
    // TODO: 18/01/2022 adicionar qual o horario incial do alarme, pesquisar como programar um alarme, como pegar valor restante e notificação

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
    }
    //binding.dataSpinner?.onItemSelectedListener = this
    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }


}

// TODO: 19/01/2022 arrumar large_adicionar

//        mPickTimeBtn.setOnClickListener {
//            val cal = Calendar.getInstance()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY, hour)
//                cal.set(Calendar.MINUTE, minute)
//                textView.text = SimpleDateFormat("HH:mm").format(cal.time)
//            }
//            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()