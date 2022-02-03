package com.sgs.devcamp2.flametalk_android.ui.createopenchatprofile

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sgs.devcamp2.flametalk_android.databinding.FragmentCreateOpenChatProfileBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateOpenChatProfile : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCreateOpenChatProfileBinding
    private val model by viewModels<CreateOpenChatProfileViewModel>()
    private val getProfileImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                model.setProfileImage(uriToPath(result.data!!.data!!))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateOpenChatProfileBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }
    fun initUI() {
        binding.etCreateOpenChatRoomProfileName.setText("") // user 이름 가져오기
        binding.etCreateOpenChatRoomProfileName.onTextChanged {
            model.updateName(it.toString())
        }
        binding.etCreateOpenChatRoomProfileDescription.onTextChanged {
            model.updateDescription(it.toString())
        }
        binding.tvCreateOpenChatRoomSubmit.setOnClickListener(this)
        binding.ivCreateOpenChatRoomBackgroundImgSelect.setOnClickListener(this)
        // initObserve()
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
                                    // progress bar visible gone
                                }
                            is UiState.Loading ->
                                {
                                    // progress bar visible visible
                                }
                            is UiState.Error ->
                                {
                                    // progress bar visible gone
                                }
                        }
                    }
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvCreateOpenChatRoomSubmit ->
                {
                    model.createOpenProfile()
                }
            binding.ivCreateOpenChatRoomBackgroundImgSelect ->
                {
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

        private const val PROFILE_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
