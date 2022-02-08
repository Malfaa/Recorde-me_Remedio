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
import com.malfaa.lembrete.R
import com.malfaa.lembrete.calendario
import com.malfaa.lembrete.databinding.AlterarFragmentBinding
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaEscolhida
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.minutoEscolhido
import com.malfaa.lembrete.relogio
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

    binding.textView?.setOnClickListener {
        relogio().show(requireParentFragment().parentFragmentManager, "lembrete")

        relogio().addOnPositiveButtonClickListener {
            horaEscolhida = String.format("%02d", relogio().hour)  // TODO: alterar o adicionar com a fun relogio() caso funcione
            minutoEscolhido = String.format("%02d", relogio().minute)

            viewModel.horarioFinal.value = "$horaEscolhida:$minutoEscolhido"

            binding.textView?.text = viewModel.horarioFinal.value
            Log.d("Valores Relógio", viewModel.horarioFinal.value.toString())
        }
    }

        // TODO: alterar esse fragment e xml

//        binding.alterar.setOnClickListener {
//            try {
//                viewModel.alterarLembrete(
//                    ItemEntidade(
//                        args.item.id,
//                        binding.campoRemedio.text.toString().replaceFirstChar { it.uppercase() },
//                        viewModel.horarioFinal.value.toString(),
//                        calendario(binding.dataSpinner.selectedItemPosition),
//                        binding.horaSpinner.selectedItemPosition,
//                        binding.dataSpinner.selectedItemPosition,
//                        binding.campoNota.text.toString()
//                    )
//                )
//
//                MainViewModel.alarmeVar.value = true
//                AdicionarFragment.spinnerHora.value = binding.horaSpinner.selectedItemPosition
//                AdicionarFragment.remedio.value = binding.campoRemedio.text.toString()
//                AdicionarFragment.nota.value = binding.campoNota.text.toString()
//
//                this.findNavController().navigate(AlterarFragmentDirections.actionAlterarFragmentToMainFragment())
//            }catch (e: Exception){
//                Log.d("Error Alterar", e.toString())
//            }
//        }


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
        binding.textView?.text = args.item.horaInicial
    }
}