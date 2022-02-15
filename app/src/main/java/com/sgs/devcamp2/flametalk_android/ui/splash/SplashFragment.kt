package com.sgs.devcamp2.flametalk_android.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author 박소연
 * @created 2022/02/15
 * @desc 스플래시 화면
 */

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initSplash()
        return binding.root
    }

    private fun initSplash() {

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.navigation_signin)
        }, 1500)
    }
}
