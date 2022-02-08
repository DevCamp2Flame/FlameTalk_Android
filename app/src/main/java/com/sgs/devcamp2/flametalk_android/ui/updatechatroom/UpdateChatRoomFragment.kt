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
import com.sgs.devcamp2.flametalk_android.databinding.FragmentUpdateChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateChatRoomFragment :
    Fragment(),
    CompoundButton.OnCheckedChangeListener,
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
                if (args.userChatRoom.thumbnail.size == 1) {
                    if (binding.layoutPersonOneImage.root.visibility == View.GONE) {
                        binding.layoutPersonOneImage.root.visibility == View.VISIBLE
                        binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                        initGlidePersonOne()
                    }
                } else if (args.userChatRoom.thumbnail.size == 1) {
                    binding.layoutPersonTwoImage.root.visibility == View.VISIBLE
                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                    initGlidePersonTwo()
                } else if (args.userChatRoom.thumbnail.size == 3) {
                    binding.layoutPersonThreeImage.root.visibility == View.VISIBLE
                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                    initGlidePersonThree()
                } else {
                    binding.layoutPersonOneImage.root.visibility == View.VISIBLE
                    binding.ivUpdateChatRoomProfileImage.visibility = View.GONE
                    initGlidePersonFour()
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
        binding.swUpdateChatRoomLock.setOnCheckedChangeListener(this)
        binding.layoutUpdateChatRoomArrowSpace.setOnClickListener(this)
        binding.etUpdateChatRoomTitleName.onTextChanged {
            model.updateTitle(it.toString())
        }
        binding.tvUpdateChatRoomSubmit.setOnClickListener(this)
        binding.etUpdateChatRoomTitleName.setText(args.userChatRoom.title)
        binding.swUpdateChatRoomLock.isChecked = args.userChatRoom.inputLock
        when (args.userChatRoom.thumbnail.size) {
            1 -> {
                initGlidePersonOne()
            }
            2 -> {
                initGlidePersonTwo()
            }
            3 -> {
                initGlidePersonThree()
            }
            else -> {
                initGlidePersonFour()
            }
        }
    }

    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.imageUrl.collect {
                        Glide.with(binding.ivUpdateChatRoomProfileImage).load(it)
                            .into(binding.ivUpdateChatRoomProfileImage)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.uiState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
        when (button) {
            binding.swUpdateChatRoomLock -> {
                if (isChecked) {
                    model.updateInputLock()
                } else {
                    model.updateInputLock()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.layoutUpdateChatRoomName -> {
                // 채팅방 이름 수정 창 이동
            }
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
                // 확인 버튼
                model.updateChatRoom(args.userChatroomId, args.userChatRoom.thumbnail)
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

    fun initGlidePersonOne() {
        binding.layoutPersonOneImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonOneImage.ivPersonOneImg)
            .load(args.userChatRoom.thumbnail[0])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonOneImage.ivPersonOneImg)
        binding.layoutPersonOneImage.root.setOnClickListener(this)
    }
    fun initGlidePersonTwo() {
        binding.layoutPersonTwoImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonTwoImage.ivPersonTwoImg1)
            .load(args.userChatRoom.thumbnail[0])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonTwoImage.ivPersonTwoImg1)
        Glide.with(binding.layoutPersonTwoImage.ivPersonTwoImg2)
            .load(args.userChatRoom.thumbnail[1])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonTwoImage.ivPersonTwoImg2)
        binding.layoutPersonTwoImage.root.setOnClickListener(this)
    }
    fun initGlidePersonThree() {
        binding.layoutPersonThreeImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg1)
            .load(args.userChatRoom.thumbnail[0])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg1)
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg2)
            .load(args.userChatRoom.thumbnail[1])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg2)
        Glide.with(binding.layoutPersonThreeImage.ivPersonThreeImg3)
            .load(args.userChatRoom.thumbnail[2])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonThreeImage.ivPersonThreeImg3)
        binding.layoutPersonThreeImage.root.setOnClickListener(this)
    }
    fun initGlidePersonFour() {
        binding.layoutPersonFourImage.root.visibility = View.VISIBLE
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg1)
            .load(args.userChatRoom.thumbnail[0])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonFourImage.ivPersonFourImg1)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg2)
            .load(args.userChatRoom.thumbnail[1])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonFourImage.ivPersonFourImg2)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg3)
            .load(args.userChatRoom.thumbnail[2])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonFourImage.ivPersonFourImg3)
        Glide.with(binding.layoutPersonFourImage.ivPersonFourImg4)
            .load(args.userChatRoom.thumbnail[3])
            .transform(CenterCrop(), RoundedCorners(35))
            .into(binding.layoutPersonFourImage.ivPersonFourImg4)
        binding.layoutPersonFourImage.root.setOnClickListener(this)
    }

    companion object {
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
