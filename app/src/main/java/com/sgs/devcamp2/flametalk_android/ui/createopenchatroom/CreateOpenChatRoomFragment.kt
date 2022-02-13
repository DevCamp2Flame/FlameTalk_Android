package com.sgs.devcamp2.flametalk_android.ui.createopenchatroom

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.databinding.FragmentCreateOpenChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/02/02
 */
@AndroidEntryPoint
class CreateOpenChatRoomFragment :
    Fragment(),
    View.OnClickListener,
    CreateOpenChatRoomAdapter.ItemClickCallBack {

    lateinit var binding: FragmentCreateOpenChatRoomBinding
    lateinit var createOpenChatRoomAdapter: CreateOpenChatRoomAdapter
    lateinit var adapter: CreateOpenChatRoomAdapter
    private val model by viewModels<CreateOpenChatRoomViewModel>()

    private val getBackgroundImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                model.setBackgroundImage(uriToPath(result.data!!.data!!))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateOpenChatRoomBinding.inflate(inflater, container, false)
        initUI(this.requireContext())
        initObserve()
        viewLifecycleOwner.lifecycleScope.launch {
            model.backgroundImage.collect {
                if (it !== "") {
                    Glide.with(binding.ivCreateOpenChatRoomBackgroundImg)
                        .load(it)
                        .into(binding.ivCreateOpenChatRoomBackgroundImg)
                }
            }
        }

        return binding.root
    }

    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.openProfileList.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            adapter.submitList(state.data.openProfiles)
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.createOpenCHatRoomUiState.collect {
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
    fun initUI(context: Context) {
        Glide.with(context)
            .load("https://picsum.photos/250/250")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivCreateOpenChatRoomBackgroundImgSelect.visibility = View.GONE
                    binding.pbCreateOpenChatRoomLoading.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivCreateOpenChatRoomBackgroundImgSelect.visibility = View.VISIBLE
                    binding.pbCreateOpenChatRoomLoading.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivCreateOpenChatRoomBackgroundImg)
        adapter = CreateOpenChatRoomAdapter(this)
        binding.rvCreateOpenChatRoomProfile.layoutManager = GridLayoutManager(context, 2)
        binding.rvCreateOpenChatRoomProfile.adapter = adapter
        binding.ivCreateOpenChatRoomBackgroundImgSelect?.setOnClickListener(this)
        binding.etCreateOpenChatRoomTitle.onTextChanged {
            model.updateTitle(it.toString())
        }
        model.getOpenProfileList()
        binding.tvCreateOpenChatRoomSubmit.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.ivCreateOpenChatRoomBackgroundImgSelect ->
                {
                    getProfileImage(2)
                }
            binding.tvCreateOpenChatRoomSubmit ->
                {
                    model.createOpenChatRoom()
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
        val TAG: String = "로그"
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }

    override fun onItemClicked(openProfile: OpenProfile) {
        model.updateSelectedOpenProfile(openProfile)
    }
}
