package com.malfaa.recorde_me_remedio.fragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.SplashScreenFragmentBinding
import com.malfaa.recorde_me_remedio.room.LDatabase
import com.malfaa.recorde_me_remedio.viewmodel.SplashScreenViewModel
import com.malfaa.recorde_me_remedio.viewmodelfactory.SplashScreenViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    companion object {
        fun newInstance() = SplashScreen()
    }

    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var binding: SplashScreenFragmentBinding
    private lateinit var viewModelFactory: SplashScreenViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.splash_screen_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = SplashScreenViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashScreenViewModel::class.java]

        val animacao = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade)
        binding.logo.startAnimation(animacao)


        Handler(Looper.getMainLooper()).postDelayed({
            this.findNavController().navigate(
                SplashScreenDirections.actionSplashScreenToMainFragment())
        }, 1400)
    }

    }
