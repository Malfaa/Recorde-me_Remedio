package com.malfaa.recorde_me_remedio.inicial

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.SplashScreenFragmentBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(){

    private lateinit var binding : SplashScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = SplashScreenFragmentBinding.inflate(inflater, container, false)

        binding.clock.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)

        Handler(Looper.getMainLooper()).postDelayed({
            this.findNavController().navigate(
                SplashScreenDirections.actionSplashScreenToMainFragment())
        }, 1400)

        return binding.root
    }
}