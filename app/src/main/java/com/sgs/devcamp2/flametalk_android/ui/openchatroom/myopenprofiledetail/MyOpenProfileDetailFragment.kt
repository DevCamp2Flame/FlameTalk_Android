package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofiledetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.databinding.FragmentMyOpenProfileDetailBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOpenProfileDetailFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentMyOpenProfileDetailBinding
    private val args by navArgs<MyOpenProfileDetailFragmentArgs>()
    private val model by viewModels<MyOpenProfileDetailViewModel>()

    val TAG: String = "로그"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOpenProfileDetailBinding.inflate(inflater, container, false)
        initObserve()
        initUI()
        return binding.root
    }

    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.uiState.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    Log.d(TAG, "MyOpenProfileDetailFragment - initObserve() called")
                                    binding.tvMyOpenProfileDetailName.text = state.data.nickname
                                    binding.tvMyOpenProfileDetailDescription.text = state.data.description
                                    Glide.with(this@MyOpenProfileDetailFragment).load(state.data.imageUrl)
                                        .into(binding.ivMyOpenProfileDetailImage)
                                }
                        }
                    }
                }
                launch {
                    model.deleteUiState.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    findNavController().popBackStack()
                                }
                        }
                    }
                }
            }
        }
    }

    fun initUI() {
        model.initOpenProfile(args.openProfile.openProfileId)
        binding.ivMyOpenProfileDetailBack.setOnClickListener(this)
        binding.ivMyOpenProfileDetailDelete.setOnClickListener(this)
        binding.ivMyOpenProfileDetailEdit.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivMyOpenProfileDetailBack -> {
                findNavController().popBackStack()
            }
            binding.ivMyOpenProfileDetailDelete -> {
                // 오픈 프로필 삭제
                model.deleteOpenProfile(args.openProfile.openProfileId)
            }
            binding.ivMyOpenProfileDetailEdit ->
                {
                    val action = MyOpenProfileDetailFragmentDirections.actionNavigationMyOpenProfileDetailToEditMyOpenProfileDetailFragment(
                        args.openProfile
                    )
                    findNavController().navigate(action)
                }
        }
    }
}
