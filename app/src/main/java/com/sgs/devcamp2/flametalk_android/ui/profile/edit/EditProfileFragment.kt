package com.sgs.devcamp2.flametalk_android.ui.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/18
 * @desc 프로필 수정 페이지 (배경 이미지, 프로필 이미지, 상태메세지)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class EditProfileFragment : Fragment() {
    private val binding by lazy { FragmentEditProfileBinding.inflate(layoutInflater) }
    private val viewModel: EditProfileViewModel by activityViewModels()
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateUI()
        initUI()
        initClickEvent()
        return binding.root
    }

    private fun initUI() {
        // 수정할 유저 정보 담기
        if (args.userInfo != null && viewModel.userProfile.value == null) {
            viewModel.setUserProfile(args.userInfo!!)
        }
    }

    // ViewModel StateFlow 변수가 갱신되면 UI에 자동 바인딩한다
    private fun updateUI() {
        lifecycleScope.launchWhenResumed {
            // 닉네임
            viewModel.nickname.collectLatest {
                binding.tvEditProfileNickname.text = it
            }
        }

        lifecycleScope.launchWhenResumed {
            // 상태메세지
            viewModel.description.collectLatest {
                binding.tvEditProfileDesc.text = it
            }
        }
        lifecycleScope.launchWhenResumed {
            // 프로필 사진
            viewModel.profileImage.collectLatest {
                Glide.with(binding.imgEditProfile)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.imgEditProfile)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.backgroundImage.collectLatest {
                Glide.with(binding.imgEditProfileBg)
                    .load(it)
                    .into(binding.imgEditProfileBg)
            }
        }
    }

    private fun initClickEvent() {
        binding.tvEditProfileClose.setOnClickListener {
            findNavController().navigateUp()
        }

        // 프로필 이미지 변경
        binding.imgEditProfileGallery.setOnClickListener {
            // TODO: 프로필 이미지 가져오기경
            // getProfileImage(PROFILE_IMAGE)
        }

        // 배경 이미지 변경
        binding.imgEditProfileGalleryBg.setOnClickListener {
            // TODO: 배경 이미지 가져오기
            // getProfileImage(BACKGROUND_IMAGE)
        }

        // 상태 메세지 변경
        binding.tvEditProfileDesc.setOnClickListener {
            val editProfileToDescDirections: NavDirections =
                EditProfileFragmentDirections.actionProfileToEditDesc(viewModel.description.value)
            findNavController().navigate(editProfileToDescDirections)
        }

        // 프로필 수정 완료
        binding.tvEditProfileConfirm.setOnClickListener {
            // TODO: 프로필 편집 통신
        }
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
