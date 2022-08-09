package com.malfaa.recorde_me_remedio.remedio.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.repository.Repository

class MainFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

//    private val adapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = MainFragmentBinding.inflate(inflater,container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.recyclerview.adapter = adapter

        viewModel.listaRemedio.observe(viewLifecycleOwner){
            remedios ->
//            adapter.submitList(remedios)
        }

        //Navegar até Adicionar
        binding.adicionarLembrete?.setOnClickListener{
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }
        binding.adicionarLembreteLand?.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }


        //Navega até Alterar
        viewModel.recebeRemedio.observe(viewLifecycleOwner){
                id ->
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAlterarFragment(id))
        }
    }
}