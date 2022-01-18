package com.malfaa.lembrete.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malfaa.lembrete.R
import com.malfaa.lembrete.viewmodel.AdicionarViewModel

class AdicionarFragment : Fragment() {

    companion object {
        fun newInstance() = AdicionarFragment()
    }

    private lateinit var viewModel: AdicionarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adicionar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdicionarViewModel::class.java)

    }

}