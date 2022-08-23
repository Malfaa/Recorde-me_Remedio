package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.malfaa.recorde_me_remedio.*
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel.Companion.horaInicial
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarViewModel.Companion.minutoInicial
import com.malfaa.recorde_me_remedio.repository.Repository
import java.time.LocalDateTime
import java.util.*

class AdicionarFragment : Fragment() {
    private lateinit var binding: AdicionarFragmentBinding

    private val viewModel: AdicionarViewModel by viewModels {
        AdicionarViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

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
            picker()
        }

        binding.adicionar.setOnClickListener{
            try{
                val remedio = Remedio(
                    0,
                    binding.campoRemedio.text.toString(),
                    binding.horaEditText.text.toString().toInt(),
                    binding.dataEditText.text.toString().toInt(),
                    binding.horarioInicial.text.toString(),
                    binding.campoNota.text.toString()
                //binding.todosOsDias.isChecked
                ).apply {
                    primeiroDia = diaAtual()
                    ultimoDia = diaFinal(binding.dataEditText.text.toString())
                }

                viewModel.adicionarRemedio(remedio)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Campo necessário inválido, tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }// TODO: ideia é quando o checkBox estiver marcado, ele muda o dataEditText pro valor que refere à todos_os_dias

        viewModel.checkBox.observe(viewLifecycleOwner){
                condicao ->
            when(condicao){
                true -> binding.dataEditText.isEnabled = false
                false -> binding.dataEditText.isEnabled = true

            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }

    private fun picker() {
        var horaFinal: String
        val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Hora em que iniciará:").setHour(LocalDateTime.now().hour)
                .setMinute(LocalDateTime.now().minute).build()
        } else {
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Hora em que iniciará:").setHour(horaFormato(Date().time))
                .setMinute(minutoFormato(Date().time)).build()
        }

        picker.show(requireParentFragment().parentFragmentManager, "remedio")

        picker.addOnPositiveButtonClickListener {
            horaInicial = String.format("%02d", picker.hour)
            minutoInicial = String.format("%02d", picker.minute)
            horaFinal = "$horaInicial:$minutoInicial"
            binding.horarioInicial.text = horaFinal
        }
    }

}