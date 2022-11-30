package com.malfaa.recorde_me_remedio.remedio.main

import android.app.AlarmManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.alarme.AlarmeService
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding
import com.malfaa.recorde_me_remedio.google.ADMOB.ad
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.remedio.main.MainAdapter.RemedioListener
import com.malfaa.recorde_me_remedio.remedio.main.MainViewModel.Companion.deletar
import com.malfaa.recorde_me_remedio.remedio.main.MainViewModel.Companion.remedioItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding : MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater,container, false)

        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.remedio_lembrar)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        viewModel.criandoCanalDeNotificacao(requireContext())

        //Verifica se pode ou não ser usado o exact alarm em api's > 31
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(requireContext(), AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context?.startActivity(intent)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainAdapter(
            RemedioListener{
                    remedio ->
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAlterarFragment(remedio)
                )
            }
        )

        ad(binding, requireContext())

        binding.recyclerview.adapter = adapter

        viewModel.listaRemedio.observe(viewLifecycleOwner){
                remedios ->
            adapter.submitList(remedios)
        }


        binding.adicionarLembrete?.setOnClickListener{
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }
        binding.adicionarLembreteLand?.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }


        deletar.observe(viewLifecycleOwner){
                condicao ->
            if(condicao){
                alertDialogDeletarContato(remedioItem.value!!)
                deletar.value = false
            }
        }

        //Callback
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        callback.isEnabled
    }

    private fun alertDialogDeletarContato(remedio: Remedio){
        val construtor = AlertDialog.Builder(requireActivity())

        construtor.setTitle(R.string.deletar)
        construtor.setMessage(R.string.deletar_descricao)
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            try{
                viewModel.deletarRemedio(remedio)
                AlarmeService().removerAlarme(requireContext(), remedio)
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