package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
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
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.lembrete.R
import com.malfaa.lembrete.calendario
import com.malfaa.lembrete.databinding.AlterarFragmentBinding
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.dataCustomClicado
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaCustomClicado
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaEscolhida
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.minutoEscolhido
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AlterarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodelfactory.AlterarViewModelFactory

class AlterarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: AlterarViewModel
    private lateinit var binding: AlterarFragmentBinding
    private lateinit var viewModelFactory: AlterarViewModelFactory
    private val args: AlterarFragmentArgs by navArgs()

    private lateinit var picker: MaterialTimePicker


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.alterar_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Alterar Lembrete"

        bindingInfos()

        return binding.root
    }

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

        if(args.item.verificaDataCustom){
            dataCustomClicado.value = args.item.verificaDataCustom
            binding.customData.isChecked
            binding.dataEditText.setText(args.item.data.toString())
            binding.dataSpinner.visibility = View.GONE
            binding.dataEditText.visibility = View.VISIBLE
        }else{
            dataCustomClicado.value = args.item.verificaDataCustom
            binding.dataSpinner.setSelection(args.item.data,true)
            binding.dataEditText.text?.isEmpty()
            binding.dataSpinner.visibility = View.VISIBLE
            binding.dataEditText.visibility = View.GONE
        }
        if(args.item.verificaHoraCustom){
            horaCustomClicado.value = args.item.verificaHoraCustom
            //binding.customHora.isChecked
            binding.customHora.isChecked
            binding.horaEditText.setText(args.item.hora.toString())
            AdicionarFragment.horaParaAlarme.value = 0
            binding.horaSpinner.visibility = View.GONE
            binding.horaEditText.visibility = View.VISIBLE
        }else{
            horaCustomClicado.value = args.item.verificaHoraCustom
            binding.horaSpinner.setSelection(args.item.hora,true)
            binding.horaEditText.text?.isEmpty()
            binding.horaSpinner.visibility = View.VISIBLE
            binding.horaEditText.visibility = View.GONE
        }

        binding.textView.setOnClickListener {
            picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText("Hora em que iniciará:").setHour(12)
                .setMinute(0).build()

            picker.show(requireParentFragment().parentFragmentManager, "lembrete")


            picker.addOnPositiveButtonClickListener {
                horaEscolhida = String.format("%02d", picker.hour)
                minutoEscolhido = String.format("%02d", picker.minute)
                viewModel.horarioFinal.value = "$horaEscolhida:$minutoEscolhido"

                binding.textView.text = viewModel.horarioFinal.value
                Log.d("Valores Relógio", viewModel.horarioFinal.value.toString())
            }
        }

        binding.customHora.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                horaCustomClicado.value = isChecked
                AdicionarFragment.horaParaAlarme.value = 0
                binding.horaSpinner.visibility = View.GONE
                binding.horaEditText.visibility = View.VISIBLE
            }else{
                horaCustomClicado.value = false
                binding.horaEditText.text?.isEmpty()
                binding.horaSpinner.visibility = View.VISIBLE
                binding.horaEditText.visibility = View.GONE
            }
        }

        binding.customData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                dataCustomClicado.value = isChecked
                binding.dataSpinner.visibility = View.GONE
                binding.dataEditText.visibility = View.VISIBLE
            }else{
                dataCustomClicado.value = false
                binding.dataEditText.text?.isEmpty()
                binding.dataSpinner.visibility = View.VISIBLE
                binding.dataEditText.visibility = View.GONE
            }
        }

        binding.alterar.setOnClickListener {
            try {
                if(horaCustomClicado.value!! && dataCustomClicado.value!!){ //positivos
                    viewModel.alterarLembrete(ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString().replaceFirstChar { it.uppercase() },
                        viewModel.horarioFinal.value.toString(),
                        calendario(binding.dataEditText.text.toString().toInt(),dataCustomClicado.value!!),
                        binding.horaEditText.text.toString().toInt(),
                        binding.dataEditText.text.toString().toInt(),
                        binding.campoNota.text.toString(),
                        horaCustomClicado.value!!,
                        dataCustomClicado.value!!
                    ))
                }else if(!horaCustomClicado.value!! && dataCustomClicado.value!!){// falso positivo
                    viewModel.alterarLembrete(ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString().replaceFirstChar { it.uppercase() },
                        viewModel.horarioFinal.value.toString(),
                        calendario(binding.dataEditText.text.toString().toInt(),dataCustomClicado.value!!),
                        binding.horaSpinner.selectedItemPosition,
                        binding.dataEditText.text.toString().toInt(),
                        binding.campoNota.text.toString(),
                        horaCustomClicado.value!!,
                        dataCustomClicado.value!!
                    ))
                }else if(horaCustomClicado.value!! && !dataCustomClicado.value!!){//positvo falso
                    viewModel.alterarLembrete(ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString().replaceFirstChar { it.uppercase() },
                        viewModel.horarioFinal.value.toString(),
                        calendario(binding.dataSpinner.selectedItemPosition,dataCustomClicado.value!!),
                        binding.horaEditText.text.toString().toInt(),
                        binding.dataSpinner.selectedItemPosition,
                        binding.campoNota.text.toString(),
                        horaCustomClicado.value!!,
                        dataCustomClicado.value!!
                    ))
                }else{ //negativos
                    viewModel.alterarLembrete(ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString().replaceFirstChar { it.uppercase() },
                        viewModel.horarioFinal.value.toString(),
                        calendario(binding.dataSpinner.selectedItemPosition,dataCustomClicado.value!!),
                        binding.horaSpinner.selectedItemPosition,
                        binding.dataSpinner.selectedItemPosition,
                        binding.campoNota.text.toString(),
                        horaCustomClicado.value!!,
                        dataCustomClicado.value!!
                    ))
                }

                AdicionarFragment.horaParaAlarme.value = if(binding.horaSpinner.selectedItemPosition == 0){
                    binding.horaEditText.text.toString().toInt()
                }else{
                    binding.horaSpinner.selectedItemPosition
                }

                MainViewModel.alarmeVar.value = true
                AdicionarFragment.remedio.value = binding.campoRemedio.text.toString()
                AdicionarFragment.nota.value = binding.campoNota.text.toString()

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

    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }

    private fun bindingInfos(){
        binding.campoRemedio.setText(args.item.remedio)
        binding.campoNota.setText(args.item.nota)
        binding.textView.text = args.item.horaInicial
    }
}