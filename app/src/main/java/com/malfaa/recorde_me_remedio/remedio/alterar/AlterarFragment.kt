package com.malfaa.recorde_me_remedio.remedio.alterar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malfaa.recorde_me_remedio.databinding.AlterarFragmentBinding

class AlterarFragment : Fragment()  {
    private lateinit var binding : AlterarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AlterarFragmentBinding.inflate(inflater,container, false)

        return binding.root
    }
}