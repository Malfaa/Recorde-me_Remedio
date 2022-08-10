package com.malfaa.recorde_me_remedio.remedio.alterar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.AlterarFragmentBinding
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.remedio.adicionar.AdicionarFragment.Companion.EDITOR_TEXT_INSTANCE
import com.malfaa.recorde_me_remedio.repository.Repository

class AlterarFragment : Fragment()  {
    private lateinit var binding : AlterarFragmentBinding

    private val viewModel: AlterarViewModel by viewModels{
        AlterarViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

//    private val arguments : AlterarFragmentArgs by navArgs()

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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}