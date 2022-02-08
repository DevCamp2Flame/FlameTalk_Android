package com.sgs.devcamp2.flametalk_android.ui.profile.add

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentAddProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/25
 * @desc 프로필 생성 페이지 (배경 이미지, 프로필 이미지, 상태메세지)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddProfileFragment : Fragment() {
    private val binding by lazy { FragmentAddProfileBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<AddProfileViewModel>()
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
        // TODO: userDAO에서 로컬에 저장된 nickname을 가져온 후 바인딩
    }

    // ViewModel StateFlow 변수가 갱신되면 UI에 자동 바인딩한다
    private fun updateUI() {
        // 닉네임
        lifecycleScope.launchWhenResumed {
            viewModel.nickname.collectLatest {
                binding.tvAddProfileNickname.text = it
            }
        }

        // 상태메세지
        lifecycleScope.launchWhenResumed {
            viewModel.description.collectLatest {
                binding.tvAddProfileDesc.text = it
            }
        }

        // 프로필 이미지
        lifecycleScope.launchWhenResumed {
            viewModel.profileImage.collectLatest {
                Glide.with(binding.imgAddProfile)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.imgAddProfile)
            }
        }

        // 배경 이미지
        lifecycleScope.launchWhenResumed {
            viewModel.backgroundImage.collectLatest {
                Glide.with(binding.imgAddProfileBg)
                    .load(it)
                    .into(binding.imgAddProfileBg)
            }
        }

        // 프로필 생성 성공 여부
        lifecycleScope.launchWhenResumed {
            viewModel.isSuccess.collectLatest {
                if (it != null) {
                    if (it) {
                        val addProfileToFriendDirections: NavDirections =
                            AddProfileFragmentDirections.actionAddToFriend(true)
                        findNavController().navigate(addProfileToFriendDirections)
                    } else {
                        val addProfileToFriendDirections: NavDirections =
                            AddProfileFragmentDirections.actionAddToFriend(false)
                        findNavController().navigate(addProfileToFriendDirections)
                    }
                }
            }
        }
    }

    private fun initClickEvent() {
        binding.tvAddProfileClose.setOnClickListener {
            findNavController().popBackStack()
        }

        // 프로필 이미지 변경
        binding.imgAddProfileGallery.setOnClickListener {
            getProfileImage(PROFILE_IMAGE)
        }

        // 배경 이미지 변경
        binding.imgAddProfileGalleryBg.setOnClickListener {
            getProfileImage(BACKGROUND_IMAGE)
        }

        // 상태메세지 생성
        binding.tvAddProfileDesc.setOnClickListener {
            val addProfileToDescDirections: NavDirections =
                AddProfileFragmentDirections.actionAddToEditDesc(startView = "Add")
            findNavController().navigate(addProfileToDescDirections)
        }

        // 프로필 생성 완료
        binding.tvAddProfileConfirm.setOnClickListener {
            // 파일 생성 통신 요청
            viewModel.addProfile()
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
