package com.malfaa.recorde_me_remedio.remedio.adicionar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding

class AdicionarFragment : Fragment()  {
    private lateinit var binding : AdicionarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AdicionarFragmentBinding.inflate(inflater,container, false)

        return binding.root
    }
}