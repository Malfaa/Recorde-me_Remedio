package com.malfaa.recorde_me_remedio.remedio.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = MainFragmentBinding.inflate(inflater,container, false)

        return binding.root
    }
}