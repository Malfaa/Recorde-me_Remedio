package com.malfaa.lembrete.fragment

import android.annotation.SuppressLint
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.lembrete.R
import com.malfaa.lembrete.calendario
import com.malfaa.lembrete.calendarioParaData
import com.malfaa.lembrete.databinding.AdicionarFragmentBinding
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.LDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.AdicionarViewModel
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alarmeVar
import com.malfaa.lembrete.viewmodelfactory.AdicionarViewModelFactory
import java.util.*

class AdicionarFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        lateinit var horaEscolhida: String
        lateinit var minutoEscolhido: String
        val horaParaAlarme = MutableLiveData<Int>()
        val remedio = MutableLiveData<String>()
        val nota = MutableLiveData<String>()
        val horaCustomClicado = MutableLiveData(false)
        val dataCustomClicado = MutableLiveData(false)
    }

    private lateinit var picker: MaterialTimePicker

    private lateinit var binding: AdicionarFragmentBinding
    private lateinit var viewModel: AdicionarViewModel
    private lateinit var viewModelFactory: AdicionarViewModelFactory

    private lateinit var conjuntoDatas: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.adicionar_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Novo Lembrete"

        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.adicionarAdView.loadAd(adRequest)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = AdicionarViewModelFactory(ItemRepository(dataSource))
        viewModel = ViewModelProvider(this, viewModelFactory)[AdicionarViewModel::class.java]

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

        ad()

        binding.textView.setOnClickListener {
            picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText("Hora em que iniciará:").setHour(12)
                .setMinute(0).build()

            picker.show(requireParentFragment().parentFragmentManager, "lembrete")


            picker.addOnPositiveButtonClickListener{
                horaEscolhida = String.format("%02d", picker.hour)
                minutoEscolhido = String.format("%02d", picker.minute)
                viewModel.horarioFinal.value = "$horaEscolhida:$minutoEscolhido"

                binding.textView.text = viewModel.horarioFinal.value
                Log.d("Valores Relógio", viewModel.horarioFinal.value.toString())
            }

        }

        binding.customHora.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                horaCustomClicado.value = isChecked
                horaParaAlarme.value = 0
                binding.horaSpinner.visibility = View.GONE
                binding.horaEditText.visibility = View.VISIBLE
            }else{
                horaCustomClicado.value = false
                binding.horaEditText.text?.isEmpty()
                binding.horaSpinner.visibility = View.VISIBLE
                binding.horaEditText.visibility = View.GONE
            }
        }

        binding.customData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                dataCustomClicado.value = isChecked
                binding.dataSpinner.visibility = View.GONE
                binding.dataEditText.visibility = View.VISIBLE
            }else{
                dataCustomClicado.value = false
                binding.dataEditText.text?.isEmpty()
                binding.dataSpinner.visibility = View.VISIBLE
                binding.dataEditText.visibility = View.GONE
            }
        }

        binding.adicionar.setOnClickListener {
            if (binding.textView.text.isEmpty()
                || binding.remedioTexto.text.isEmpty()
                || (binding.horaEditText.text.isNullOrEmpty() && binding.horaSpinner.selectedItemPosition == 0)
                || (binding.customData.text.isNullOrEmpty() && binding.dataSpinner.selectedItemPosition == 0)
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

                        viewModel.adicionandoLembrete(
                            ItemEntidade(
                                0,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaEditText.text.toString().toInt(),
                                binding.dataEditText.text.toString().toInt(),
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!
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

                        viewModel.adicionandoLembrete(
                            ItemEntidade(
                                0,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaSpinner.selectedItemPosition,
                                binding.dataEditText.text.toString().toInt(),
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!
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
                        viewModel.adicionandoLembrete(
                            ItemEntidade(
                                0,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaEditText.text.toString().toInt(),
                                binding.dataSpinner.selectedItemPosition,
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!
                            )
                        )
                    } else { //negativos
                        val calendario = Calendar.getInstance()
                        conjuntoDatas =
                            (if (horarioEscolhidoConcatenado < horarioLocalConcatenado) {
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
                        viewModel.adicionandoLembrete(
                            ItemEntidade(
                                0,
                                binding.campoRemedio.text.toString()
                                    .replaceFirstChar { it.uppercase() },
                                viewModel.horarioFinal.value.toString(),
                                conjuntoDatas,
                                binding.horaSpinner.selectedItemPosition,
                                binding.dataSpinner.selectedItemPosition,
                                binding.campoNota.text.toString(),
                                horaCustomClicado.value!!,
                                dataCustomClicado.value!!
                            )
                        )

                    }

                    horaParaAlarme.value = if (binding.horaSpinner.selectedItemPosition == 0) {
                        binding.horaEditText.text.toString().toInt()
                    } else {
                        binding.horaSpinner.selectedItemPosition
                    }

                    alarmeVar.value = true

                    //aqui
                    //passa aqui o id para alarme

                    remedio.value = binding.campoRemedio.text.toString()
                    nota.value = binding.campoNota.text.toString()

                    this.findNavController()
                        .navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
                    Toast.makeText(requireContext(), "Lembrete adicionado.", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                    Toast.makeText(context, "Algum campo não preenchido.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.retornar.setOnClickListener {
            this.findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(AdicionarFragmentDirections.actionAdicionarFragmentToMainFragment())
        }
        callback.isEnabled
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemIdAtPosition(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        p0?.emptyView
    }

    private fun ad(){
        binding.adicionarAdView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
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
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}