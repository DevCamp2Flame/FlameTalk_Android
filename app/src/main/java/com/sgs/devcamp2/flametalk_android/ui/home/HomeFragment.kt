package com.sgs.devcamp2.flametalk_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sgs.devcamp2.flametalk_android.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.fragment.app.viewModels


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container,false)
        initUI()
        return binding.root
    }

    private fun initUI() {
    }
}
