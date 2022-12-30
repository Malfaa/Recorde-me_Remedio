package com.malfaa.recorde_me_remedio.google

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.malfaa.recorde_me_remedio.databinding.AdicionarFragmentBinding
import com.malfaa.recorde_me_remedio.databinding.AlterarFragmentBinding
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding

object ADMOB {
    fun ad(binding: MainFragmentBinding, context: Context){
        binding.adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.adView.resume()
                Log.d("AdLoaded","Banner adapter class name:" + binding.adView.responseInfo?.mediationAdapterClassName.toString())
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, "Ad failed to load", Toast.LENGTH_SHORT).show()//, error: $error.
                Log.e("Error", adError.message)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
//                Toast.makeText(context, "Ad Clicked.", Toast.LENGTH_SHORT).show()
                binding.adView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
//                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.adView.destroy()
            }
        }
    }

    fun ad(binding: AdicionarFragmentBinding, context: Context){
        binding.adicionarAdView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.adicionarAdView.resume()
                Log.d("AdLoaded","Banner adapter class name:" + binding.adicionarAdView.responseInfo?.mediationAdapterClassName.toString())
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, "Ad failed to load", Toast.LENGTH_SHORT).show()//, error: $error.
                Log.e("Error", adError.message)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
//                Toast.makeText(context, "Ad Clicked.", Toast.LENGTH_SHORT).show()
                binding.adicionarAdView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
//                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.adicionarAdView.destroy()
            }
        }
    }

    fun ad(binding: AlterarFragmentBinding, context: Context){
        binding.alterarAdView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.alterarAdView.resume()
                Log.d("AdLoaded","Banner adapter class name:" + binding.alterarAdView.responseInfo?.mediationAdapterClassName.toString())
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, "Ad failed to load", Toast.LENGTH_SHORT).show()//, error: $error.
                Log.e("Error", adError.message)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
//                Toast.makeText(context, "Ad Clicked.", Toast.LENGTH_SHORT).show()
                binding.alterarAdView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
//                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.alterarAdView.destroy()
            }
        }
    }
}