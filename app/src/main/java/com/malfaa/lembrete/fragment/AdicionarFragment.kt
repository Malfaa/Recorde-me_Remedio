package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.lembrete.R
import com.malfaa.lembrete.calendario
import com.malfaa.lembrete.databinding.AdicionarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.lembrete.viewmodelfactory.AdicionarViewModelFactory

class AdicionarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        lateinit var horaEscolhida: String
        lateinit var minutoEscolhido: String
        val horaParaAlarme = MutableLiveData<Int>()
        val remedio = MutableLiveData<String>()
        val nota = MutableLiveData<String>()
        val horaCustomClicado = MutableLiveData(false)
        val dataCustomClicado = MutableLiveData(false)
    }

    private lateinit var picker: MaterialTimePicker

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

        binding.textView.setOnClickListener {
            picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText("Hora em que iniciará:").setHour(12)
                .setMinute(0).build()

            picker.show(requireParentFragment().parentFragmentManager, "lembrete")

            picker.addOnPositiveButtonClickListener{
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
                horaParaAlarme.value = 0
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

        binding.adicionar.setOnClickListener {
            try {
                if(horaCustomClicado.value!! && dataCustomClicado.value!!){ //positivos
                  viewModel.adicionandoLembrete(ItemEntidade(
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
                    viewModel.adicionandoLembrete(ItemEntidade(
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
                    viewModel.adicionandoLembrete(ItemEntidade(
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
                    viewModel.adicionandoLembrete(ItemEntidade(
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

                horaParaAlarme.value = if(binding.horaSpinner.selectedItemPosition == 0){
                    binding.horaEditText.text.toString().toInt()
                }else{
                    binding.horaSpinner.selectedItemPosition
                }

                //Em teoria consertei o que faltava, falta arrumar o fix\me do utils e ver se algum
                //conversor vai quebrar quando for salvos os valores custom's, vendo isso, se não estiver
                //quebrado, resolver a notificação

                alarmeVar.value = true
                remedio.value = binding.campoRemedio.text.toString()
                nota.value = binding.campoNota.text.toString()

                this.findNavController()
                    .navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
                Toast.makeText(requireContext(), "Lembrete adicionado.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Log.d("error", e.toString())
                Toast.makeText(context, "Algum campo não preenchido.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.retornar.setOnClickListener {
            this.findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }
        callback.isEnabled
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemIdAtPosition(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }
}