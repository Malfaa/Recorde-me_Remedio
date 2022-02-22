package com.malfaa.lembrete.fragment

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.AlarmeReceiver
import com.malfaa.lembrete.R
import com.malfaa.lembrete.adapters.MainAdapter
import com.malfaa.lembrete.cancelarAlarme
import com.malfaa.lembrete.conversorPosEmMinutos
import com.malfaa.lembrete.databinding.MainFragmentBinding
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaCustomClicado
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaParaAlarme
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.deletar
import com.malfaa.lembrete.viewmodelfactory.MainViewModelFactory
import java.util.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModelFactory: MainViewModelFactory

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    companion object{
        val lembreteDestino = MutableLiveData<ItemEntidade>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Lembrar"
        criandoCanalDeNotificacao()

        return binding.root
    }
    //val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val datasource = com.malfaa.lembrete.room.LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = MainViewModelFactory(datasource)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.listaLembretes.observe(viewLifecycleOwner){
            adapter.submitList(it.toMutableList())
        }

        binding.adicionarLembrete.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }

        alarmeVar.observe(viewLifecycleOwner){condicao->   // FIXME: colocar no adicionar fragment
            if (condicao) {
                if(horaCustomClicado.value!!){
                    alarme(
                        AdicionarFragment.horaEscolhida.toLong(),
                        AdicionarFragment.minutoEscolhido.toLong(),
                        horaParaAlarme.value?.toLong()!!
                    )
                alarmeVar.value = false
                }else{
                    alarme(
                        AdicionarFragment.horaEscolhida.toLong(),
                        AdicionarFragment.minutoEscolhido.toLong(),
                        conversorPosEmMinutos(horaParaAlarme.value!!)!!
                    )

                    alarmeVar.value = false
                }
            }
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


        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        callback.isEnabled
    }

    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())

        construtor.setTitle(R.string.deletar)
        construtor.setMessage(R.string.deletar_descricao)
        deletar.value = false
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            try{
                viewModel.deletarLembrete(lembreteDestino.value!!)
                cancelarAlarme()
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

    private fun criandoCanalDeNotificacao(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nome = "LembreteNotificacao"
            val descricao = "Canal de notificacao p/ lembretes"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("notificacao", nome, importancia).apply { description = descricao }
            val gerenciadorNotificacao = getSystemService(requireContext(), NotificationManager::class.java)

            gerenciadorNotificacao?.createNotificationChannel(canal)
        }
    }

    private fun alarme(hora: Long, minutos: Long, horario: Long) {
        alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(
            requireContext(), AlarmeReceiver::class.java).let { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hora.toInt())
            set(Calendar.MINUTE, minutos.toInt())
        }
        
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * (60 * horario),  //60000 * (60 * 4) = 60000 * '240' = 144000000
            alarmIntent
        )

    }

}

// TODO: colocar ad no programa
/* FIXME: Registro: 3 problemas encontrados (L.P -> low problem | H.P -> high problem)

           - O alarme está estranho, não sei se o horário está certo, o que inicia o alarme e os que repetem...
             e quando o app está fechado ele notifica null, tbm não sei se está nos horários devidos (H.P)

           - Quando alterar o lembrete, os Switchs não trocam de estado, permanecem 'desligados' (L.P)

           - O dia setado quando adicionado não tem aplicação nenhuma dentro do app      (H.P)     } Usar um dia como setter? Não sei como se aplica ao Schedule WorkManager, talvez nem mudar por ter uma data
*/

