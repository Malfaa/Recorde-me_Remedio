package com.malfaa.lembrete.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.R
import com.malfaa.lembrete.adapters.MainAdapter
import com.malfaa.lembrete.databinding.MainFragmentBinding
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodelfactory.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun mainInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Lembrar"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val datasource = com.malfaa.lembrete.room.LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = MainViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.listaLembretes.observe(viewLifecycleOwner,{
            adapter.submitList(it.toMutableList())
        })

        //viewModel.alarme()

        binding.adicionarLembrete.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        callback.isEnabled


    }

}
// TODO: 19/01/2022 talvez abrir um pop-up com escolha entre deletar e alterar o lembrete  ou usar Swipe p/ ação, esquerda altera direita deleta (vice-versa)