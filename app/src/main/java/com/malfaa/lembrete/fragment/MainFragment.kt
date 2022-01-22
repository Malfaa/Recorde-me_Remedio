package com.malfaa.lembrete.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.R
import com.malfaa.lembrete.adapters.MainAdapter
import com.malfaa.lembrete.databinding.MainFragmentBinding
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.deletar
import com.malfaa.lembrete.viewmodelfactory.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModelFactory: MainViewModelFactory

    companion object{
        val lembreteDestino = MutableLiveData<ItemEntidade>(ItemEntidade(0, "", "", "", "",""))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Lembrar"
        criarCanalNotificacao()

        return binding.root
    }

    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nome = "lembreteApp"
            val descricao = "Canal p/ notificacao"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("notificacao", nome, importancia).apply { description = descricao }
            val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val datasource = com.malfaa.lembrete.room.LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = MainViewModelFactory(datasource)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter()
        binding.recyclerview.adapter = adapter

//        val swipeGesture = object : SwipeGesture(){
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//                when(direction){
//                    ItemTouchHelper.LEFT -> {
//                        alertDialogDeletarContato()
//                    }
//                }
//
//                super.onSwiped(viewHolder, direction)
//            }
//        }

        viewModel.listaLembretes.observe(viewLifecycleOwner,{
            adapter.submitList(it.toMutableList())
        })

        binding.adicionarLembrete.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }

        alterar.observe(viewLifecycleOwner,{
            condicao ->
            if (condicao){
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAlterarFragment(
                    lembreteDestino.value!!))
            }

        })

        deletar.observe(viewLifecycleOwner, {
                condicao ->
            if (condicao) {
                alertDialogDeletarContato()
            }
        })


        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        callback.isEnabled

        //pegar os valores do item e passar como bundle
        //this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAlterarFragment())

    }

    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())

        construtor.setTitle(R.string.deletar)
        construtor.setMessage(R.string.deletar_descricao)
        deletar.value = false
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            try{
                viewModel.deletarLembrete(lembreteDestino.value!!)
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
// TODO: 19/01/2022 talvez abrir um pop-up com escolha entre deletar e alterar o lembrete  ou usar Swipe p/ ação, esquerda altera direita deleta (vice-versa)
// TODO: 20/01/2022 tracking dos timers/alarmes
// TODO: 22/01/2022 mini tutorial right & left swipe