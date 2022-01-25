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
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.malfaa.lembrete.R
import com.malfaa.lembrete.databinding.AlterarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AlterarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodelfactory.AlterarViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AlterarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: AlterarViewModel
    private lateinit var binding: AlterarFragmentBinding
    private lateinit var viewModelFactory: AlterarViewModelFactory
    private val args: AlterarFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.alterar_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Alterar Lembrete"

        Log.d("ARgumentos", args.item.toString())

        bindingInfos()

        return binding.root
    }
// mindset é primeiro o recurso, logo, var e depois o insert, logo, atribuir o valor requerido
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = AlterarViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AlterarViewModel::class.java]

        alterar.value = false

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

        binding.dataSpinner.setSelection(args.item.data,true)
        binding.horaSpinner.setSelection(args.item.hora,true)

    binding.horaInicialValue.setOnClickListener {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val text = SimpleDateFormat("HH:mm").format(cal.time)
            AdicionarFragment.horaEscolhida = hour.toLong()
            AdicionarFragment.minutoEscolhido = minute.toLong()
            viewModel.horarioFinal.value = text
        }
        TimePickerDialog(this.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
            Calendar.MINUTE), true).show()

        //Under Develpment
//        relogioPicker()
//        Log.d("Antes de alterar", args.item.horaInicial.toString())
//        AdicionarFragment.horaEscolhida = relogioPicker().hora.toLong()
//        AdicionarFragment.minutoEscolhido = relogioPicker().minuto.toLong()
//        viewModel.horarioFinal.value = relogioPicker().horarioFinal
//        Log.d("Depois de alterar", viewModel.horarioFinal.value.toString())
    }

        binding.alterar.setOnClickListener {
            try {
                viewModel.alterarLembrete(
                    ItemEntidade(
                        args.item.id,
                        binding.campoRemedio.text.toString(),
                        viewModel.horarioFinal.value.toString(),
                        binding.horaSpinner.selectedItemPosition,
                        binding.dataSpinner.selectedItemPosition,
                        binding.campoNota.text.toString()
                    )
                )
                this.findNavController().navigate(AlterarFragmentDirections.actionAlterarFragmentToMainFragment())
            }catch (e: Exception){
                Log.d("Error Alterar", e.toString())
            }

        }


        binding.retornar.setOnClickListener {
            this.findNavController().navigate(AlterarFragmentDirections.actionAlterarFragmentToMainFragment())
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(AlterarFragmentDirections.actionAlterarFragmentToMainFragment())
        }
        callback.isEnabled
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemIdAtPosition(p2)

    }
    //binding.dataSpinner?.onItemSelectedListener = this
    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }

    private fun bindingInfos(){
        binding.campoRemedio.setText(args.item.remedio)
        binding.campoNota.setText(args.item.nota)

        //binding.horaInicialValue.text = args.item.horaInicial fixme arrumar aqui
    }
}

// FIXME: 24/01/2022 mudei utils, itemEntidade, adicionar e alterar