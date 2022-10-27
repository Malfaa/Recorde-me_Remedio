package com.malfaa.recorde_me_remedio.admob

import android.content.Context
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.malfaa.recorde_me_remedio.databinding.MainFragmentBinding

object admob {
    fun ad(binding: MainFragmentBinding, context: Context){
        binding.adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(context, "Ad loaded.", Toast.LENGTH_SHORT).show()
                binding.adView.resume()
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                val error = String.format(
                    "domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}")
                Toast.makeText(context, "Ad failed to load", Toast.LENGTH_SHORT).show()//, error: $error.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Toast.makeText(context, "Ad Clicked.", Toast.LENGTH_SHORT).show()
                binding.adView.pause()

            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Toast.makeText(context, "Ad closed.", Toast.LENGTH_SHORT).show()
                binding.adView.destroy()
            }
        }
    }
}