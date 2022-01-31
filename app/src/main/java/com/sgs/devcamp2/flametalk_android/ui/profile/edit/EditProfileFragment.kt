package com.sgs.devcamp2.flametalk_android.ui.profile.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private val viewModel by activityViewModels<EditProfileViewModel>()
    private val args: EditProfileFragmentArgs by navArgs()
    private val getProfileImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                viewModel.setProfileImage(uriToPath(result.data!!.data!!))
            }
        }
    private val getBackgroundImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                viewModel.setBackgroundImage(uriToPath(result.data!!.data!!))
            }
        }

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
            // TODO: 프로필 이미지 가져오기
            getProfileImage(PROFILE_IMAGE)
        }

        // 배경 이미지 변경
        binding.imgEditProfileGalleryBg.setOnClickListener {
            // TODO: 배경 이미지 가져오기
            // getProfileImage(BACKGROUND_IMAGE)
        }

        // 상태 메세지 변경
        binding.tvEditProfileDesc.setOnClickListener {
            val editProfileToDescDirections: NavDirections =
                EditProfileFragmentDirections.actionEditToEditDesc(desc = viewModel.description.value, startView = "Edit")
            findNavController().navigate(editProfileToDescDirections)
        }

        // 프로필 수정 완료
        binding.tvEditProfileConfirm.setOnClickListener {
            // TODO: 프로필 편집 통신
            // 파일 통신을 위한 임시 요청
            // File Create 통신
            if (viewModel.profileImage.value != ""){
                viewModel.postCreateImage()
            }
        }
    }

    // 이미지 변경
    private fun getProfileImage(type: Int) {
        // TODO: SDK 버전에 따라 외장 저장소 접근 로직을 다르게 처리 (Android 10(Q, 29) 이상/미만)
        if (checkPermission()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            intent.putExtra("crop", true)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            when (type) {
                PROFILE_IMAGE -> {
                    getProfileImageLauncher.launch(intent)
                }
                BACKGROUND_IMAGE -> {
                    getBackgroundImageLauncher.launch(intent)
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 저장소 접근 권한
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(requireActivity(), "권한이 거부되었습니다. 직접 권한을 허용해야 합니다.", Toast.LENGTH_SHORT)
                .show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CALLBACK_CONSTANT
            )
        }
    }

    // image uri를 path로 전환
    @SuppressLint("Range")
    private fun uriToPath(uri: Uri): String {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToNext()
        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))
        cursor?.close()

        if (path.isNullOrEmpty()) {
            return ""
        }
        return path
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
