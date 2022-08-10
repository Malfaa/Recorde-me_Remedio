package com.malfaa.recorde_me_remedio.remedio.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.remedio.main.MainAdapter.*
import com.malfaa.recorde_me_remedio.repository.Repository

class MainFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

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

        val adapter = MainAdapter(
            RemedioListener { remedio ->
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAlterarFragment(remedio)
                )
            },
            LongRemedioListener {remedio ->
                alertDialogDeletarContato(remedio)
            }
        )

        binding.recyclerview.adapter = adapter

        viewModel.listaRemedio.observe(viewLifecycleOwner){
            remedios ->
            adapter.submitList(remedios)
        }

        //Navegar até Adicionar
        binding.adicionarLembrete?.setOnClickListener{
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }
        binding.adicionarLembreteLand?.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }


    }

    private fun alertDialogDeletarContato(remedio: Remedio){
        val construtor = AlertDialog.Builder(requireActivity())

        construtor.setTitle(R.string.deletar)
        construtor.setMessage(R.string.deletar_descricao)
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            try{
                viewModel.deletarRemedio(remedio)
//                AlarmService(requireContext()).removerAlarme(remedio.requestCode)
                Toast.makeText(context, "Lembrete Deletado.", Toast.LENGTH_SHORT).show()
                dialogInterface.cancel()
            }catch (e: Exception){
                Log.d("Error Del", e.toString())
            }
        }
        construtor.setNegativeButton("Cancelar"){
                dialogInterface:DialogInterface, _: Int ->
            dialogInterface.cancel()
        }

        val alerta = construtor.create()
        alerta.show()
    }
}