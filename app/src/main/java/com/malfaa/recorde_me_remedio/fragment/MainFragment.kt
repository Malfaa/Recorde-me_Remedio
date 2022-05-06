package com.malfaa.recorde_me_remedio.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.malfaa.lembrete.R
import com.malfaa.lembrete.databinding.MainFragmentBinding
import com.malfaa.recorde_me_remedio.adapters.MainAdapter
import com.malfaa.recorde_me_remedio.repository.ItemRepository
import com.malfaa.recorde_me_remedio.room.LDatabase
import com.malfaa.recorde_me_remedio.room.entidade.ItemEntidade
import com.malfaa.recorde_me_remedio.servico.AlarmService
import com.malfaa.recorde_me_remedio.viewmodel.MainViewModel
import com.malfaa.recorde_me_remedio.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.recorde_me_remedio.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.recorde_me_remedio.viewmodel.MainViewModel.Companion.deletar
import com.malfaa.recorde_me_remedio.viewmodelfactory.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var viewModel        :MainViewModel
    private lateinit var binding          :MainFragmentBinding
    private lateinit var viewModelFactory :MainViewModelFactory

    companion object{
        val lembreteDestino             = MutableLiveData<ItemEntidade>()
        val expandValue                 = MutableLiveData<Boolean>()
        lateinit var viewlifeCycleOwner   :LifecycleOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Lembre-me"

        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        criandoCanalDeNotificacao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val datasource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = MainViewModelFactory(ItemRepository(datasource))
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter()
        binding.recyclerview.adapter = adapter

        viewlifeCycleOwner = viewLifecycleOwner

        //AdMob function
        ad()

        //RecyclerView população
        viewModel.listaLembretes.observe(viewLifecycleOwner){
            adapter.submitList(it.toMutableList())
        }

        alarmeVar.observe(viewLifecycleOwner){condicao->
            if (condicao) {
                viewModel.adicionarAlarmeItens(requireContext())
                alarmeVar.value = false
            }
        }

        //Navegação p/ outros fragments
        binding.adicionarLembrete?.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }
        binding.adicionarLembreteLand?.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }

        alterar.observe(viewLifecycleOwner) { condicao ->
            if (condicao) {
                this.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAlterarFragment(
                        lembreteDestino.value!!
                    )
                )
                Log.d("Valor", lembreteDestino.value.toString())
            }
        }
        deletar.observe(viewLifecycleOwner) { condicao ->
            if (condicao) {
                alertDialogDeletarContato()
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

    //Alarme bloco
    private fun criandoCanalDeNotificacao(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nome = "recorde-meRemedio"
            val descricao = "Canal de notificacao p/ lembretes"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("recorde-meremedio", nome, importancia).apply { description = descricao }
            val gerenciadorNotificacao = getSystemService(requireContext(), NotificationManager::class.java)

            gerenciadorNotificacao?.createNotificationChannel(canal)
        }
    }

    // TODO: Criar um database perfil, que terá uma var boolean que identificará se foi aceito os termos ou não, caso seja, não mostrar a tela de termos. A ideia é deixar o usuário com informação à respeito dos defeitos do app, que estão descrevidos nas notas 1..3


    private fun ad(){
        binding.adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.adView.resume()
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                val error = String.format(
                    "domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}")
                Toast.makeText(context, "Ad failed to load, error: $error.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Toast.makeText(context, "Ad Clicked.", Toast.LENGTH_SHORT).show()
                binding.adView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.adView.destroy()
            }
        }
    }

    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())

        construtor.setTitle(R.string.deletar)
        construtor.setMessage(R.string.deletar_descricao)
        deletar.value = false
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            try{
                viewModel.deletarLembrete(lembreteDestino.value!!)
                AlarmService(requireContext()).removerAlarme(lembreteDestino.value!!.requestCode)
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