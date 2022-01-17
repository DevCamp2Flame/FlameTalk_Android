package com.sgs.devcamp2.flametalk_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.databinding.FragmentProfileBinding
import com.sgs.devcamp2.flametalk_android.util.swapViewVisibility
import com.sgs.devcamp2.flametalk_android.util.toInvisible
import com.sgs.devcamp2.flametalk_android.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc 앱 실행 시 기본 화면. 1번째 탭의 친구 목록 페이지
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initViewType()
        initUserProfile()

        binding.imgProfileClose.setOnClickListener {
            findNavController().navigateUp()
        }
       /* binding.ckbProfileBookmark.isSelected = false
        binding.ckbProfileBookmark.setOnClickListener {
            it.
        }*/
    }

    // 메인 유저, 친구 여부에 따라 UI가 다름
   private fun initViewType() {
        when (args.viewType) {
            1 -> { // 내 프로필
                binding.ckbProfileBookmark.toInvisible()
                swapViewVisibility(binding.cstProfileChat, binding.cstProfileEdit)
            }
            2 -> { // 친구 프로필
                binding.ckbProfileBookmark.toVisible()
                swapViewVisibility(binding.cstProfileEdit, binding.cstProfileChat)
            }
        }
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.userProfile.collectLatest {
                Glide.with(binding.imgProfile)
                    .load(it.image).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgProfile)
                binding.tvProfileNickname.text = it.nickname
            }
        }
    }
}
