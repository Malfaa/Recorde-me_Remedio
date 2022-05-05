package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.lembrete.*
import com.malfaa.lembrete.databinding.AlterarFragmentBinding
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.dataCustomClicado
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaCustomClicado
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.horaEscolhida
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.minutoEscolhido
import com.malfaa.lembrete.fragment.AdicionarFragment.Companion.requestRandomCode
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AlterarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodelfactory.AlterarViewModelFactory
import java.time.LocalDateTime
import java.util.*

class AlterarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: AlterarViewModel
    private lateinit var binding: AlterarFragmentBinding
    private lateinit var viewModelFactory: AlterarViewModelFactory
    private val args: AlterarFragmentArgs by navArgs()

    private lateinit var picker: MaterialTimePicker

    private lateinit var conjuntoDatas: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.alterar_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Alterar Lembrete"

        bindingInfos()

        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.alterarAdView.loadAd(adRequest)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = AlterarViewModelFactory(ItemRepository(dataSource))
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

        if (args.item.verificaDataCustom) {
//            dataCustomClicado.value = args.item.verificaDataCustom

//            binding.customData.isChecked

            binding.dataEditText.setText(args.item.data.toString())
//            binding.dataSpinner.visibility = View.GONE
//            binding.dataEditText.visibility = View.VISIBLE
        } else {
//            dataCustomClicado.value = args.item.verificaDataCustom
            binding.dataSpinner.setSelection(args.item.data, true)
            binding.dataEditText.text?.isEmpty()
//            binding.dataSpinner.visibility = View.VISIBLE
//            binding.dataEditText.visibility = View.GONE
        }

        ad()


        if (args.item.verificaHoraCustom) {
//            horaCustomClicado.value = args.item.verificaHoraCustom
            binding.horaEditText.setText(args.item.hora.toString())
            AdicionarFragment.horaParaAlarme.value = 0
//            binding.horaSpinner.visibility = View.GONE
//            binding.horaEditText.visibility = View.VISIBLE

        } else {
//            horaCustomClicado.value = args.item.verificaHoraCustom
            binding.horaSpinner.setSelection(args.item.hora, true)
            binding.horaEditText.text?.isEmpty()
//            binding.horaSpinner.visibility = View.VISIBLE
//            binding.horaEditText.visibility = View.GONE
        }


        binding.horarioInicial.setOnClickListener {
            picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
                    .setMinute(LocalDateTime.now().minute).build()
            }else{
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Hora em que iniciará:").setHour(horaFormato(Date().time))
                    .setMinute(minutoFormato(Date().time)).build()
            }

            picker.show(requireParentFragment().parentFragmentManager, "lembrete")

            picker.addOnPositiveButtonClickListener {
                horaEscolhida = String.format("%02d", picker.hour)
                minutoEscolhido = String.format("%02d", picker.minute)
                viewModel.horarioFinal.value = "$horaEscolhida:$minutoEscolhido"

                binding.horarioInicial.text = viewModel.horarioFinal.value
                Log.d("Valores Relógio", viewModel.horarioFinal.value.toString())
            }
        }

        binding.customHora.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                horaCustomClicado.value = isChecked
                AdicionarFragment.horaParaAlarme.value = 0
                binding.horaSpinner.visibility = View.GONE
                binding.horaEditText.visibility = View.VISIBLE
            } else {
                horaCustomClicado.value = false
                binding.horaEditText.text?.isEmpty()
                binding.horaSpinner.visibility = View.VISIBLE
                binding.horaEditText.visibility = View.GONE
            }
        }

        binding.customData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dataCustomClicado.value = isChecked
                binding.dataSpinner.visibility = View.GONE
                binding.dataEditText.visibility = View.VISIBLE
            } else {
                dataCustomClicado.value = false
                binding.dataEditText.text?.isEmpty()
                binding.dataSpinner.visibility = View.VISIBLE
                binding.dataEditText.visibility = View.GONE
            }
        }

        binding.alterar.setOnClickListener {
            if (binding.horarioInicial.text.isEmpty()
                || binding.remedioTexto.text.isEmpty()
                || (binding.horaEditText.text.isNullOrEmpty() && binding.horaSpinner.selectedItemPosition == 0)
                || (binding.dataEditText.text.isNullOrEmpty() && binding.dataSpinner.selectedItemPosition == 0)
            ) {
                Toast.makeText(context, "Preencha os campos necessários.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val horarioEscolhidoConcatenado =
                        horaEscolhida.toInt() + minutoEscolhido.toInt()//"${horaEscolhida.toInt()}"+"${minutoEscolhido.toInt()}".toInt()
                    val horarioLocalConcatenado =
                        Calendar.HOUR_OF_DAY + Calendar.MINUTE//"${Calendar.HOUR_OF_DAY}"+"${Calendar.MINUTE}".toInt()
                    if (horaCustomClicado.value!! && dataCustomClicado.value!!) { //positivos
                        val calendario = Calendar.getInstance()
                        conjuntoDatas =
                            if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
                                calendario.add(Calendar.DATE, 1)
                                String.format(
                                    "${calendarioParaData(calendario.time)} - ${
                                        calendario(
                                            binding.dataEditText.text.toString().toInt(),
                                            dataCustomClicado.value!!
                                        )
                                    }"
                                )
                            } else {
                                String.format(
                                    "${calendarioParaData(calendario.time)} - ${
                                        calendario(
                                            binding.dataEditText.text.toString().toInt(),
                                            dataCustomClicado.value!!
                                        )
                                    }"
                                )
                            }

                        viewModel.alterarLembrete(
                            ItemEntidade(
                                args.item.id,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaEditText.text.toString().toInt(),
                                binding.dataEditText.text.toString().toInt(),
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!,
                                args.item.requestCode
                            )
                        )
                    } else if (!horaCustomClicado.value!! && dataCustomClicado.value!!) {// hora = falso && data = positivo
                        val calendario = Calendar.getInstance()
                        conjuntoDatas =
                            if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
                                calendario.add(Calendar.DATE, 1)
                                String.format(
                                    "${calendarioParaData(calendario.time)} - ${
                                        calendario(
                                            binding.dataEditText.text.toString().toInt(),
                                            dataCustomClicado.value!!
                                        )
                                    }"
                                )
                            } else {
                                String.format(
                                    "${calendarioParaData(calendario.time)} - ${
                                        calendario(
                                            binding.dataEditText.text.toString().toInt(),
                                            dataCustomClicado.value!!
                                        )
                                    }"
                                )
                            }

                        viewModel.alterarLembrete(
                            ItemEntidade(
                                args.item.id,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaSpinner.selectedItemPosition,
                                binding.dataEditText.text.toString().toInt(),
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!,
                                args.item.requestCode
                            )
                        )
                    } else if (horaCustomClicado.value!! && !dataCustomClicado.value!!) {// hora = positvo && data = falso
                        val calendario = Calendar.getInstance()
                        conjuntoDatas =
                            if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
                                calendario.add(Calendar.DATE, 1)
                                when (binding.dataSpinner.selectedItemPosition) {
                                    5 -> calendarioParaData(calendario.time)
                                    else -> String.format(
                                        "${calendarioParaData(calendario.time)} - ${
                                            calendario(
                                                binding.dataSpinner.selectedItemPosition,
                                                dataCustomClicado.value!!
                                            )
                                        }"
                                    )
                                }
                            } else {
                                when (binding.dataSpinner.selectedItemPosition) {
                                    5 -> calendarioParaData(calendario.time)
                                    else -> String.format(
                                        "${calendarioParaData(calendario.time)} - ${
                                            calendario(
                                                binding.dataSpinner.selectedItemPosition,
                                                dataCustomClicado.value!!
                                            )
                                        }"
                                    )
                                }
                            }
                        viewModel.alterarLembrete(
                            ItemEntidade(
                                args.item.id,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaEditText.text.toString().toInt(),
                                binding.dataSpinner.selectedItemPosition,
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!,
                                args.item.requestCode
                            )
                        )
                    } else { //negativos
                        val calendario = Calendar.getInstance()
                        conjuntoDatas = (if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
                            calendario.add(Calendar.DATE, 1)
                            when (binding.dataSpinner.selectedItemPosition) {
                                5 -> calendarioParaData(calendario.time)
                                else -> String.format(
                                    "${calendarioParaData(calendario.time)} - ${
                                        calendario(
                                            binding.dataSpinner.selectedItemPosition,
                                            dataCustomClicado.value!!
                                        )
                                    }"
                                )
                            }
                        } else {
                            when (binding.dataSpinner.selectedItemPosition) {
                                5 -> calendarioParaData(calendario.time)
                                else -> String.format(
                                    "${calendarioParaData(calendario.time)} - " +
                                            calendario(
                                                binding.dataSpinner.selectedItemPosition,
                                                dataCustomClicado.value!!
                                            )
                                )
                            }
                        })
                        viewModel.alterarLembrete(
                            ItemEntidade(
                                args.item.id,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaSpinner.selectedItemPosition,
                                binding.dataSpinner.selectedItemPosition,
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!,
                                args.item.requestCode
                            )
                        )

                    }

                    AdicionarFragment.horaParaAlarme.value =
                        if (binding.horaSpinner.selectedItemPosition == 0) {
                            binding.horaEditText.text.toString().toInt()
                        } else {
                            binding.horaSpinner.selectedItemPosition
                        }

                    alarmeVar.value = true

                    this.findNavController()
                        .navigate(AlterarFragmentDirections.actionAlterarFragmentToMainFragment())
                    Toast.makeText(requireContext(), "Lembrete alterado.", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Log.d("Error Alterar", e.toString())
                }}
        }

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

    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }

    private fun bindingInfos(){
        binding.campoRemedio.setText(args.item.remedio)
        binding.campoNota.setText(args.item.nota)
        binding.horarioInicial.text = args.item.horaInicial
    }
    private fun ad(){
        binding.alterarAdView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.alterarAdView.resume()
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
                binding.alterarAdView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.alterarAdView.destroy()
            }
        }
    }
}