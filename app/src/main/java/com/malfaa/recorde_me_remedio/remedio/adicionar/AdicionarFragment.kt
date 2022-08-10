package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding
import com.malfaa.recorde_me_remedio.local.RemedioDatabase
import com.malfaa.recorde_me_remedio.repository.Repository

class AdicionarFragment : Fragment()  {
    private lateinit var binding : AdicionarFragmentBinding

    private val viewModel: AdicionarViewModel by viewModels{
        AdicionarViewModelFactory(Repository(RemedioDatabase.getInstance(requireContext())))
    }

    companion object{
        const val EDITOR_TEXT_INSTANCE = "EDITOR_TEXT_INSTANCE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AdicionarFragmentBinding.inflate(inflater,container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState != null){
            binding.item = savedInstanceState.getParcelable(EDITOR_TEXT_INSTANCE)
        }



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EDITOR_TEXT_INSTANCE, binding.item)
    }
}