package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.databinding.FragmentMyOpenChatProfileBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.chattingviewpager.ChattingViewPagerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/01/26
 */

@AndroidEntryPoint
class MyOpenChatProfileFragment :
    Fragment(),
    MyOpenChatProfileAdapter.ItemClickCallBack,
    View.OnClickListener {
    val TAG: String = "로그"
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
        initObserve()
        return binding.root
    }
    fun initUI() {
        binding.rvMyOpenChatProfile.layoutManager = GridLayoutManager(context, 2)
        openChatProfileAdapter = MyOpenChatProfileAdapter(this)

        binding.rvMyOpenChatProfile.adapter = openChatProfileAdapter
        binding.tvCreateOpenChatProfile.setOnClickListener(this)
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.initOpenProfile()
                    Log.d(TAG, "MyOpenChatProfileFragment - initObserve() called")
                }
                launch {
                    Log.d(TAG, "MyOpenChatProfileFragment - initObserve() called")
                    viewModel.uiState.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    Log.d(TAG, "MyOpenChatProfileFragment - ${state.data} called")
                                    openChatProfileAdapter.submitList(state.data)
                                }
                        }
                    }
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvCreateOpenChatProfile ->
                {
                    findNavController().navigate(R.id.navigation_create_open_chat_profile)
                }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MyOpenChatProfileFragment - onStart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MyOpenChatProfileFragment - onPause() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MyOpenChatProfileFragment - onResume() called")
    }
    override fun onItemClicked(openProfile: OpenProfile) {
        var action = ChattingViewPagerFragmentDirections.actionNavigationChattingViewPagerFragmentToNavigationMyOpenProfileDetail(openProfile)
        findNavController().navigate(action)
    }
}
