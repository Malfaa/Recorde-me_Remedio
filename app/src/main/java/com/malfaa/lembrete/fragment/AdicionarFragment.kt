package com.malfaa.lembrete.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.R
import com.malfaa.lembrete.databinding.AdicionarFragmentBinding
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodelfactory.AdicionarViewModelFactory

class AdicionarFragment : Fragment() {

    companion object {
        fun newInstance() = AdicionarFragment()
    }

    private lateinit var binding: AdicionarFragmentBinding
    private lateinit var viewModel: AdicionarViewModel
    private lateinit var viewModelFactory: AdicionarViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.adicionar_fragment, container, false)
        return binding.root
    }

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
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.dataSpinner?.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.list_horario,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.horaSpinner?.adapter = adapter
        }

        binding.adicionar?.setOnClickListener {
            //viewModel.adicionandoLembrete()
        }

        binding.retornar?.setOnClickListener {
            this.findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }

    }

    // TODO: 18/01/2022 adicionar qual o horario incial do alarme, pesquisar como programar um alarme, como pegar valor restante e notificação
    class AcaoSpinner: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)

        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
}