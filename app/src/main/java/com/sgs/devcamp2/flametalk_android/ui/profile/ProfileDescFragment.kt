package com.sgs.devcamp2.flametalk_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sgs.devcamp2.flametalk_android.databinding.FragmentProfileDescBinding
import com.sgs.devcamp2.flametalk_android.ui.profile.add.AddProfileViewModel
import com.sgs.devcamp2.flametalk_android.ui.profile.edit.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/18
 * @desc 프로필 상태메세지 수정 페이지 (상태메세지)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileDescFragment : Fragment() {
    private val binding by lazy { FragmentProfileDescBinding.inflate(layoutInflater) }

    // AddProfileFragment와 ProfileDescFragment가 AㅇㅇProfileViewModel을 공유
    private val addViewModel: AddProfileViewModel by activityViewModels()

    // EditProfileFragment와 ProfileDescFragment가 EditProfileViewModel를 공유
    private val editViewModel: EditProfileViewModel by activityViewModels()
    private val args: ProfileDescFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.edtEditProfileDesc.setText(args.desc)

        binding.tvEditProfileDescClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgEditProfileDescDelete.setOnClickListener {
            binding.edtEditProfileDesc.setText("")
        }

        // 접근한 View에 상관 없이 이전 View로 데이터를 전달할 수 있다.
        binding.tvEditProfileDescConfirm.setOnClickListener {
            when (args.startView) {
                "Add" -> addViewModel.setProfileDesc(binding.edtEditProfileDesc.text.toString())
                "Edit" -> editViewModel.setProfileDesc(binding.edtEditProfileDesc.text.toString())
            }
            findNavController().navigateUp()
        }

        binding.edtEditProfileDesc.doOnTextChanged { text, _, _, count ->
            binding.tvEditProfileDescCount.text = "${text?.length}/60"
        }

        // 입력 문자 counter
        lifecycleScope.launchWhenStarted {
            when (args.startView) {
                "Add" -> {
                    addViewModel.description.collectLatest {
                        binding.tvEditProfileDescCount.text =
                            "${binding.edtEditProfileDesc.text.length}/60"
                    }
                }
                "Edit" -> {
                    editViewModel.description.collectLatest {
                        binding.tvEditProfileDescCount.text =
                            "${binding.edtEditProfileDesc.text.length}/60"
                    }
                }
            }
        }
    }
}
