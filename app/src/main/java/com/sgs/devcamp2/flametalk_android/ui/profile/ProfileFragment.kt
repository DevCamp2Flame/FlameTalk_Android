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
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentProfileBinding
import com.sgs.devcamp2.flametalk_android.util.swapViewVisibility
import com.sgs.devcamp2.flametalk_android.util.toInvisible
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/17
 * @created 2022/01/31
 * @desc 프로필 상세 보기 페이지
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ProfileViewModel>()
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
        binding.imgProfileBookmark.setOnClickListener {
            it.isActivated = !it.isActivated
            Snackbar.make(requireContext(), it, it.isActivated.toString(), Snackbar.LENGTH_SHORT)
                .show()
        }
        binding.cstProfileEdit.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToEdit(args.userInfo))
        }
    }

    // 메인 유저, 친구 여부에 따라 UI가 다름
    private fun initViewType() {
        Timber.d("UserInfo" + args.userInfo)
        when (args.viewType) {
            1 -> { // 내 프로필
                binding.imgProfileBookmark.toInvisible()
                binding.imgProfileFriend.toVisibleGone()
                binding.tvProfileFriend.toVisibleGone()
                swapViewVisibility(binding.cstProfileChat, binding.cstProfileEdit)
            }
            2 -> { // 친구 프로필
                binding.imgProfileBookmark.toVisible()
                swapViewVisibility(binding.cstProfileEdit, binding.cstProfileChat)
            }
            3 -> { // 내 멀티 프로필
                binding.imgProfileBookmark.toInvisible()
                binding.imgProfileFriend.toVisible()
                binding.tvProfileFriend.toVisible()
            }
        }
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        viewModel.getProfileData(profileId = 1)

        lifecycleScope.launchWhenCreated {
            viewModel.nickname.collectLatest {
                binding.tvProfileNickname.text = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.description.collectLatest {
                binding.tvProfileDesc.text = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.profileImage.collectLatest {
                Glide.with(binding.imgProfile)
                    .load(it).apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.imgProfile)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.backgroundImage.collectLatest {
                Glide.with(binding.imgProfileBg).load(it)
                    .into(binding.imgProfileBg)
            }
        }
    }
}
