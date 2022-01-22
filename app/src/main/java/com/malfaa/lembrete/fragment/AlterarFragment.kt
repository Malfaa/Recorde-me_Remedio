package com.malfaa.lembrete.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.malfaa.lembrete.R
import com.malfaa.lembrete.conversorStringEmId
import com.malfaa.lembrete.conversorStringEmMinutos
import com.malfaa.lembrete.databinding.AlterarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.viewmodel.AlterarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodelfactory.AlterarViewModelFactory

class AlterarFragment : Fragment() {

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = AlterarViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AlterarViewModel::class.java]

        alterar.value = false
        bindingDasInfos()

    }


    private fun bindingDasInfos(){
        binding.campoRemedio.setText(args.item.remedio)
        binding.campoNota.setText(args.item.remedio)
        binding.dataSpinner.getItemAtPosition(conversorStringEmId(args.item.data))
        binding.horaSpinner.getItemAtPosition(conversorStringEmId(args.item.hora))
        //binding.horaInicialValue.text = args.item.horaInicial fixme arrumar aqui
    }
}