package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.alarme.AlarmeService
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding
import com.malfaa.recorde_me_remedio.diaAtual
import com.malfaa.recorde_me_remedio.diaFinal
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.picker

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
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
        binding.retornar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.horarioInicial.setOnClickListener {
            picker(requireParentFragment().parentFragmentManager, binding.horarioInicial)
        }

        binding.adicionar.setOnClickListener{
            try{
                val remedio: Remedio
                when(binding.checkBox!!.isChecked){
                    false ->remedio = Remedio(
                        0,
                        binding.campoRemedio.text.toString(),
                        binding.horaEditText.text.toString().toInt(),
                        binding.dataEditText.text.toString().toInt(),
                        binding.horarioInicial.text.toString(),
                        binding.campoNota.text.toString(),
                        binding.checkBox!!.isChecked,
                        viewModel.getUniqueId()
                    ).apply {
                        primeiroDia = diaAtual()
                        ultimoDia = diaFinal(binding.dataEditText.text.toString())
                    }
                    true -> {
                        remedio = Remedio(
                            0,
                            binding.campoRemedio.text.toString(),
                            binding.horaEditText.text.toString().toInt(),
                            999999999,
                            binding.horarioInicial.text.toString(),
                            binding.campoNota.text.toString(),
                            binding.checkBox!!.isChecked,
                            viewModel.getUniqueId()
                        ).apply {
                            primeiroDia = diaAtual()
                            ultimoDia = "-"//diaFinal("999999999")
                        }
                    }
                }

                AlarmeService().adicionarAlarme(requireContext(), remedio, null)

                viewModel.adicionarRemedio(remedio)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Campo necessário inválido, tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.checkBox.observe(viewLifecycleOwner){
                condicao ->
            when(condicao){
                true ->
                    binding.dataEditText.apply {
                        isEnabled = false
                        background = context.getDrawable(R.drawable.day_add_alt_text_box_disabled)

                    }
                false ->
                    binding.dataEditText.apply {
                        isEnabled = true
                        background = context.getDrawable(R.drawable.day_add_alt_text_box)
                    }

            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}