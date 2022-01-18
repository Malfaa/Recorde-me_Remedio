package com.malfaa.lembrete.fragment

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
import com.malfaa.lembrete.R
import com.malfaa.lembrete.databinding.SplashScreenFragmentBinding
import com.malfaa.lembrete.viewmodel.SplashScreenViewModel

class SplashScreen : Fragment() {

    companion object {
        fun newInstance() = SplashScreen()
    }

    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var binding: SplashScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.splash_screen_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)

        val animacao = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade)
        binding.logo.startAnimation(animacao)


//        Handler(Looper.getMainLooper()).postDelayed({
//            this.findNavController().navigate(
//                SplashScreenFragmentDirections.actionSplashScreenFragmentToSignUpFragment())
//        }, 800)
//    }

    }
}