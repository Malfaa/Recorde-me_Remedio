package com.malfaa.recorde_me_remedio.remedio.alterar

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.alarme.AlarmeService
import com.malfaa.recorde_me_remedio.databinding.AlterarFragmentBinding
import com.malfaa.recorde_me_remedio.google.ADMOB
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarFragment.Companion.EDITOR_TEXT_INSTANCE
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel
import com.malfaa.recorde_me_remedio.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AlterarFragment : Fragment()  {
    private lateinit var binding : AlterarFragmentBinding

    private val args: AlterarFragmentArgs by navArgs()

    private val viewModel: AlterarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AlterarFragmentBinding.inflate(inflater,container, false)

        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.alterar_remedio)

        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.alterarAdView.loadAd(adRequest)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            binding.item = savedInstanceState.getParcelable(EDITOR_TEXT_INSTANCE)
        }

        viewModel.navegarDeVolta.observe(viewLifecycleOwner) { condicao ->
            if (condicao) {
                findNavController().popBackStack()
                viewModel.navegarDeVoltaFeito()
            }
        }

        ADMOB.ad(binding, requireContext())

        viewModel.item.value = args.item.apply {
            viewModel.checkBox.value = args.item.todosOsDias
            viewModel.checkBoxProxDia.value = args.item.proximoDia
        }

        viewModel.checkBox.observe(viewLifecycleOwner){
                condicao ->
            when(condicao){
                true ->{
                    binding.dataEditText.isEnabled = false
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding.dataEditText.background = requireContext().getDrawable(R.drawable.night_add_alt_text_box_disabled)
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            binding.dataEditText.background = requireContext().getDrawable(R.drawable.day_add_alt_text_box_disabled)
                        }
                    }
                }
                false ->{
                    binding.dataEditText.isEnabled = true
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding.dataEditText.background = requireContext().getDrawable(R.drawable.night_add_alt_text_box)
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            binding.dataEditText.background = requireContext().getDrawable(R.drawable.day_add_alt_text_box)
                        }
                    }
                }
            }
        }

        binding.retornar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.horarioInicial.setOnClickListener {
            timePicker(requireParentFragment().parentFragmentManager, args.item.linguagem, binding.horarioInicial, binding.diaReferencia)
        }

        binding.alterar.setOnClickListener{
            try {
                val remedio: Remedio

                val horas = if (binding.horarioInicial.text.isNullOrBlank()) {
                    args.item.horaComecoEmMillis
                } else {
                    tempoEmMilissegundos(
                        AdicionarViewModel.horaInicial.toInt(),
                        AdicionarViewModel.minutoInicial.toInt()
                    )
                }

                val horaEmHora: Int = if (binding.horaEditText.text?.isEmpty() == true) {
                    args.item.horaEmHora
                } else {
                    binding.horaEditText.text.toString().toInt()
                }

                val periodo: Int = if (binding.dataEditText.text.isNullOrBlank()) {
                    args.item.periodoDias
                } else {
                    binding.dataEditText.text.toString().toInt()
                }


                if (horaEmHora in 1..24) {
                    when (binding.checkBox.isChecked) {
                        false -> remedio = Remedio(
                            args.item.id,
                            binding.campoRemedio.text.toString().uppercase(),
                            horaEmHora,
                            periodo,
                            horas,
                            binding.campoNota.text.toString(),
                            binding.checkBox.isChecked,
                            args.item.linguagem,
                            binding.checkBoxProxDia.isChecked,
                            args.item.requestCode

                        ).apply {
                            primeiroDia = diaInicial(horas, args.item.linguagem, binding.checkBoxProxDia.isChecked)
                            ultimoDia = diaFinal(periodo.toString(), horas, args.item.linguagem, binding.checkBoxProxDia.isChecked)
                        }
                        true -> remedio = Remedio(
                            args.item.id,
                            binding.campoRemedio.text.toString().uppercase(),
                            horaEmHora,
                            999999999,
                            horas,
                            binding.campoNota.text.toString(),
                            binding.checkBox.isChecked,
                            args.item.linguagem,
                            binding.checkBoxProxDia.isChecked,
                            args.item.requestCode
                        ).apply {
                            primeiroDia = diaInicial(horas, args.item.linguagem, binding.checkBoxProxDia.isChecked)
                            ultimoDia = "-"// diaFinal("999999999")
                        }
                    }

                    when (binding.horarioInicial.text) {
                        null -> AlarmeService().alarmeAlterandoAlarme(requireContext(), remedio)
                        else -> AlarmeService().adicionarAlarme(requireContext(), remedio, horaEmHora, true, binding.checkBoxProxDia.isChecked)
                    }

                    viewModel.alterarRemedio(remedio)
                } else {
                    binding.horaEditText.text = null
                    Toast.makeText(
                        requireContext(),
                        "Horas tem que ser entre 1 e 24.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e:Exception){
                Toast.makeText(
                    requireContext(),
                    "Campo necessário inválido, tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}