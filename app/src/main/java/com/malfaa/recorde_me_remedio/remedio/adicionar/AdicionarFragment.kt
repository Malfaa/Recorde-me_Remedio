package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.alarme.AlarmeService
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding
import com.malfaa.recorde_me_remedio.google.ADMOB
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel.Companion.horaInicial
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel.Companion.minutoInicial
import com.malfaa.recorde_me_remedio.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AdicionarFragment : Fragment() {
    private lateinit var binding: AdicionarFragmentBinding

    private val viewModel: AdicionarViewModel by viewModel()

    companion object {
        const val EDITOR_TEXT_INSTANCE = "EDITOR_TEXT_INSTANCE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AdicionarFragmentBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title =
            requireContext().getString(R.string.novo_remedio)

        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adicionarAdView.loadAd(adRequest)

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

        ADMOB.ad(binding, requireContext())

        viewModel.navegarDeVolta.observe(viewLifecycleOwner) { condicao ->
            if (condicao) {
                findNavController().popBackStack()
                viewModel.navegarDeVoltaFeito()
            }
        }
        binding.retornar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.horarioInicial.setOnClickListener {
            picker(requireParentFragment().parentFragmentManager, binding.horarioInicial)
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

        binding.adicionar.setOnClickListener{
            try{
                val remedio: Remedio
                val horas = tempoEmMilissegundos(horaInicial.toInt(), minutoInicial.toInt())
                val local = Locale.getDefault().displayLanguage

                if (binding.horaEditText.text.toString().toInt() > 24){
                    Toast.makeText(requireContext(), "Hora máxima permitida é de:\n24 horas",Toast.LENGTH_SHORT).show()
                    binding.horaEditText.text = null
                }
                if(binding.horaEditText.text.toString().toInt() <= 0){
                    Toast.makeText(requireContext(), "Hora mínimo permitido é de:\n1 hora",Toast.LENGTH_SHORT).show()
                    binding.horaEditText.text = null
                }
                if (binding.dataEditText.text.toString().toInt() == 0){
                    Toast.makeText(requireContext(), "Período mínimo permitido é de:\n1 dia",Toast.LENGTH_SHORT).show()
                    binding.dataEditText.text = null
                }

                when(binding.checkBox.isChecked){
                    false ->remedio = Remedio(
                        0,
                        binding.campoRemedio.text.toString().uppercase(),
                        binding.horaEditText.text.toString().toInt(),
                        binding.dataEditText.text.toString().toInt(),
                        horas,
                        binding.campoNota.text.toString(),
                        binding.checkBox.isChecked,
                        local,
                        viewModel.getUniqueId()
                    ).apply {
                        primeiroDia = diaAtual(horas, local)
                        ultimoDia = diaFinal(binding.dataEditText.text.toString(),horas, local)
                    }
                    true -> {
                        remedio = Remedio(
                            0,
                            binding.campoRemedio.text.toString().uppercase(),
                            binding.horaEditText.text.toString().toInt(),
                            999999999,
                            horas,
                            binding.campoNota.text.toString(),
                            binding.checkBox.isChecked,
                            local,
                            viewModel.getUniqueId()
                        ).apply {
                            primeiroDia = diaAtual(horas, local)
                            ultimoDia = "-"//diaFinal("999999999")
                        }
                    }
                }

                AlarmeService().adicionarAlarme(requireContext(), remedio, null)

                viewModel.adicionarRemedio(remedio)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Campo necessário inválido, tente novamente.", Toast.LENGTH_SHORT).show()
                Log.e("AdicionarFragment",e.message!!)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}