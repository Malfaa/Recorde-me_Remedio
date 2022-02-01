package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.malfaa.lembrete.conversorStringEmMinutos
import com.malfaa.lembrete.databinding.AdicionarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.lembrete.viewmodelfactory.AdicionarViewModelFactory
import java.util.*
import kotlin.properties.Delegates

class AdicionarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        var horaEscolhida: Long by Delegates.notNull()
        var minutoEscolhido: Long by Delegates.notNull()
        val spinnerHora = MutableLiveData<Int>()
        val remedio = MutableLiveData<String>()
        val nota = MutableLiveData<String>()
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
                horaEscolhida = picker.hour.toLong()
                minutoEscolhido = picker.minute.toLong()
                viewModel.horarioFinal.value = "$horaEscolhida:$minutoEscolhido"

                binding.textView.text = viewModel.horarioFinal.value
                Log.d("Valores Relógio", viewModel.horarioFinal.value.toString())
            }

        }

        binding.adicionar.setOnClickListener {
            try {
                viewModel.adicionandoLembrete(
                    ItemEntidade(
                        0,
                        binding.campoRemedio.text.toString(),
                        viewModel.horarioFinal.value.toString(),
                        binding.horaSpinner.selectedItemPosition,
                        binding.dataSpinner.selectedItemPosition,
                        binding.campoNota.text.toString()
                    )
                )

                alarmeVar.value = true
                spinnerHora.value = binding.horaSpinner.selectedItemPosition
                remedio.value = binding.campoRemedio.text.toString()
                nota.value = binding.campoNota.text.toString()


                this.findNavController()
                    .navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
                Toast.makeText(requireContext(), "Lembrete adicionado.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Log.d("error", e.toString())
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