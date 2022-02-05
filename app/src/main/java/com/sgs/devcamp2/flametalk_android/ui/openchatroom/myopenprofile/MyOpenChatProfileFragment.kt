package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentMyOpenChatProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOpenChatProfileFragment : Fragment() {

    lateinit var binding: FragmentMyOpenChatProfileBinding
    lateinit var openChatProfileAdapter: MyOpenChatProfileAdapter
    private val viewModel: MyOpenChatProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOpenChatProfileBinding.inflate(inflater, container, false)
        initUI()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myOpenChatProfile.observe(
                viewLifecycleOwner
            ) {
                openChatProfileAdapter.data = it
            }
        }
        return binding.root
    }
    fun initUI() {
        binding.rvMyOpenChatProfile.layoutManager = LinearLayoutManager(context)

        openChatProfileAdapter = MyOpenChatProfileAdapter(this.requireContext())

        binding.rvMyOpenChatProfile.adapter = openChatProfileAdapter
    }
}
