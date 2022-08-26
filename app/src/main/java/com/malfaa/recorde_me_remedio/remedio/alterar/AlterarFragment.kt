package com.malfaa.recorde_me_remedio.remedio.alterar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.AlterarFragmentBinding
import com.malfaa.recorde_me_remedio.diaAtual
import com.malfaa.recorde_me_remedio.diaFinal
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.picker
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarFragment.Companion.EDITOR_TEXT_INSTANCE
import com.malfaa.recorde_me_remedio.repository.Repository

class AlterarFragment : Fragment()  {
    private lateinit var binding : AlterarFragmentBinding

    private val args: AlterarFragmentArgs by navArgs()

    private val viewModel: AlterarViewModel by viewModels{
        AlterarViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AlterarFragmentBinding.inflate(inflater,container, false)

        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.alterar_remedio)

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

        viewModel.item.value = args.item

        binding.alterar.setOnClickListener{
            try{
                val remedio = Remedio(
                    args.item.id,
                    binding.campoRemedio.text.toString(),
                    binding.horaEditText.text.toString().toInt(),
                    binding.dataEditText.text.toString().toInt(),
                    binding.horarioInicial.text.toString(),
                    binding.campoNota.text.toString(),
                    args.item.requestCode
                    //binding.todosOsDias.isChecked
                ).apply {
                    primeiroDia = diaAtual()
                    ultimoDia = diaFinal(binding.dataEditText.text.toString())
                }

                viewModel.alterarRemedio(remedio)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Campo necessário inválido, tente novamente.", Toast.LENGTH_SHORT).show()
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
                true -> binding.dataEditText.isEnabled = false
                false -> binding.dataEditText.isEnabled = true

            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}