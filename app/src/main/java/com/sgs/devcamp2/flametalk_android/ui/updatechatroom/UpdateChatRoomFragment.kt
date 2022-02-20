package com.sgs.devcamp2.flametalk_android.ui.updatechatroom

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
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.databinding.FragmentUpdateChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/02/07
 */
@AndroidEntryPoint
class UpdateChatRoomFragment :
    Fragment(),
    View.OnClickListener {
    lateinit var binding: FragmentUpdateChatRoomBinding
    private val model by viewModels<UpdateChatRoomViewModel>()
    private val args by navArgs<UpdateChatRoomFragmentArgs>()
    val TAG: String = "로그"
    private val getBackgroundImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                model.setBackgroundImage(uriToPath(result.data!!.data!!))
            } else {
                // RESULT_CANCLE
                lifecycleScope.launch {
                    model.thumbnailWithRoomId.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                if (state.data.thumbnailList.size == 1) {
                                    if (binding.layoutPersonOneImage.root.visibility == View.GONE) {
                                        binding.layoutPersonOneImage.root.visibility == View.VISIBLE
                                        binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                                        initGlidePersonOne(state.data.thumbnailList)
                                    }
                                } else if (state.data.thumbnailList.size == 1) {
                                    binding.layoutPersonTwoImage.root.visibility == View.VISIBLE
                                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                                    initGlidePersonTwo(state.data.thumbnailList)
                                } else if (state.data.thumbnailList.size == 3) {
                                    binding.layoutPersonThreeImage.root.visibility == View.VISIBLE
                                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                                    initGlidePersonThree(state.data.thumbnailList)
                                } else {
                                    binding.layoutPersonOneImage.root.visibility == View.VISIBLE
                                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                                    initGlidePersonFour(state.data.thumbnailList)
                                }
                            }
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateChatRoomBinding.inflate(inflater, container, false)
        initUI()
        initObserve()
        return binding.root
    }

    fun initUI() {
        binding.layoutUpdateChatRoomArrowSpace.setOnClickListener(this)
        binding.etUpdateChatRoomTitleName.onTextChanged {
            model.updateTitle(it.toString())
        }
        binding.tvUpdateChatRoomSubmit.setOnClickListener(this)

        // 썸네일 state 변경
        model.getThumbnailList(args.chatroomId)
    }

    fun initObserve() {
        /**
         * local에서 채팅방 설정 및 불러온 썸네일 사이즈에 따라서 이미지 바인딩
         */
        viewLifecycleOwner.lifecycleScope.launch {

            model.thumbnailWithRoomId.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.etUpdateChatRoomTitleName.setText(state.data.room.title)
                        when (state.data.thumbnailList.size) {
                            1 -> {
                                initGlidePersonOne(state.data.thumbnailList)
                            }
                            2 -> {
                                initGlidePersonTwo(state.data.thumbnailList)
                            }
                            3 -> {
                                initGlidePersonThree(state.data.thumbnailList)
                            }
                            else -> {
                                initGlidePersonFour(state.data.thumbnailList)
                            }
                        }
                    }
                }
            }
        }
        /**
         * 앨범에서 선택한 이미지 Glide 반영
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.imageUrl.collect {
                        Glide.with(binding.ivUpdateChatRoomProfileImage).load(it)
                            .fallback(R.drawable.ic_person_white_24)
                            .into(binding.ivUpdateChatRoomProfileImage)
                    }
                }
            }
        }
        /**
         * 업데이트 완료 처리
         */
        viewLifecycleOwner.lifecycleScope.launch {
            model.uiState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
        /**
         * 이미지가 s3에 성공적으로 올라갔을 경우
         */
        viewLifecycleOwner.lifecycleScope.launch {
            model.imageUploadState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            model.updateImageUrl(state.data.url)
                            model.updateChatRoom(args.chatroomId, model._userChatroomId.value)
                        }
                }
            }
        }
    }


    override fun onClick(view: View?) {
        when (view) {
            binding.layoutUpdateChatRoomArrowSpace -> {
                findNavController().popBackStack()
            }
            binding.ivUpdateChatRoomProfileImage -> {
                getProfileImage(2)
            }
            binding.layoutPersonOneImage.root -> {
                binding.layoutPersonOneImage.root.visibility = View.GONE
                binding.ivUpdateChatRoomProfileImage.visibility = View.VISIBLE
                getProfileImage(2)
            }
            binding.layoutPersonTwoImage.root -> {
                binding.layoutPersonTwoImage.root.visibility = View.GONE
                binding.ivUpdateChatRoomProfileImage.visibility = View.VISIBLE
                getProfileImage(2)
            }
            binding.layoutPersonThreeImage.root -> {
                binding.layoutPersonThreeImage.root.visibility = View.GONE
                binding.ivUpdateChatRoomProfileImage.visibility = View.VISIBLE
                getProfileImage(2)
            }
            binding.layoutPersonFourImage.root -> {
                binding.layoutPersonFourImage.root.visibility = View.GONE
                binding.ivUpdateChatRoomProfileImage.visibility = View.VISIBLE
                getProfileImage(2)
            }
            binding.tvUpdateChatRoomSubmit -> {

// 이미지 s3 전송
                if (model.imageUrl.value != "") {
                    // 선택한 이미지가 있을 때
                    model.updateImage()
                } else {
                    // 선택한 이미지가 없을 때 기본 이미지 반환.
                    model.updateChatRoom(args.chatroomId, model._userChatroomId.value)
                }
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

    fun initGlidePersonOne(thumbnailList: List<Thumbnail>) {
        binding.layoutPersonOneImage.root.visibility = View.VISIBLE
//        if (thumbnailList[0].image != "") {
        Glide.with(binding.layoutPersonOneImage.ivPersonOneImg)
            .load(thumbnailList[0].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonOneImage.ivPersonOneImg)
//        } else {
//            Glide.with(binding.layoutPersonOneImage.ivPersonOneImg)
//                .load(R.drawable.ic_person_white_24)
//                .transform(CenterCrop(), RoundedCorners(35))
//                .into(binding.layoutPersonOneImage.ivPersonOneImg)
//        }

        binding.layoutPersonOneImage.root.setOnClickListener(this)
    }

    fun initGlidePersonTwo(thumbnailList: List<Thumbnail>) {
        binding.layoutPersonTwoImage.root.visibility = View.VISIBLE
//        if (thumbnailList[0].image != "") {
        Glide.with(binding.layoutPersonTwoImage.ivPersonTwoImg1)
            .load(thumbnailList[0].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonTwoImage.ivPersonTwoImg1)
//        } else {
//            Glide.with(binding.layoutPersonTwoImage.ivPersonTwoImg1)
//                .load(R.drawable.ic_person_white_24)
//                .transform(CenterCrop(), RoundedCorners(35))
//                .into(binding.layoutPersonTwoImage.ivPersonTwoImg1)
//        }
//
        Glide.with(binding.layoutPersonTwoImage.ivPersonTwoImg2)
            .load(thumbnailList[1].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .placeholder(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonTwoImage.ivPersonTwoImg2)

        binding.layoutPersonTwoImage.root.setOnClickListener(this)
    }

    fun initGlidePersonThree(thumbnailList: List<Thumbnail>) {
        binding.layoutPersonThreeImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg1)
            .load(thumbnailList[0].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg1)
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg2)
            .load(thumbnailList[1].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg2)
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg3)
            .load(thumbnailList[2].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg3)
        binding.layoutPersonThreeImage.root.setOnClickListener(this)
    }

    fun initGlidePersonFour(thumbnailList: List<Thumbnail>) {
        binding.layoutPersonFourImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg1)
            .load(thumbnailList[0].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonFourImage.ivPersonFourImg1)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg2)
            .load(thumbnailList[1].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonFourImage.ivPersonFourImg2)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg3)
            .load(thumbnailList[2].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonFourImage.ivPersonFourImg3)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg4)
            .load(thumbnailList[3].image)
            .transform(CenterCrop(), RoundedCorners(35))
            .fallback(R.drawable.ic_person_white_24)
            .into(binding.layoutPersonFourImage.ivPersonFourImg4)
        binding.layoutPersonFourImage.root.setOnClickListener(this)
    }

    companion object {
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
