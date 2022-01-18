package com.malfaa.lembrete.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.malfaa.lembrete.R
import com.malfaa.lembrete.viewmodel.AlterarViewModel

class AlterarFragment : Fragment() {

    companion object {
        fun newInstance() = AlterarFragment()
    }

    private lateinit var viewModel: AlterarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alterar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AlterarViewModel::class.java]
    }
}