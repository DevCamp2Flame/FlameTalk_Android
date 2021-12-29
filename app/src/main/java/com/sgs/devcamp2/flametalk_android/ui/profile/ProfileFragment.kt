package com.sgs.devcamp2.flametalk_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sgs.devcamp2.flametalk_android.databinding.FragmentProfileBinding
import com.sgs.devcamp2.flametalk_android.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        initUI()
        return binding.root
    }

    private fun initUI() {
    }
}
