package com.sgs.devcamp2.flametalk_android.ui.profile.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
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

        initSticker()
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
                    findNavController().navigate(R.id.navigation_friend)
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

    // image uri를 path로 전환 // TODO: ViewModel로 이동
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

    private fun initSticker() {
        // 하단의 이모티콘 메뉴 버튼 // TODO: 확장성을 고려하여 RecyclerView로 변경
        Glide.with(requireContext()).load(R.drawable.emoji_aww).into(binding.imgAddProfileEmoji1)
        Glide.with(requireContext()).load(R.drawable.emoji_clap).into(binding.imgAddProfileEmoji2)
        Glide.with(requireContext()).load(R.drawable.emoji_dance).into(binding.imgAddProfileEmoji3)
        Glide.with(requireContext()).load(R.drawable.emoji_hearts).into(binding.imgAddProfileEmoji4)
        Glide.with(requireContext()).load(R.drawable.emoji_party).into(binding.imgAddProfileEmoji5)
        Glide.with(requireContext()).load(R.drawable.emoji_sad).into(binding.imgAddProfileEmoji6)

        binding.imgAddProfileEmoji1.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_AWW))
        }
        binding.imgAddProfileEmoji2.setOnClickListener {
            // binding.cstAddProfile.addView(createImageView(EMOJI_CLAP))
        }
        binding.imgAddProfileEmoji3.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_DANCE))
        }
        binding.imgAddProfileEmoji4.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_HEART))
        }
        binding.imgAddProfileEmoji5.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_PARTY))
        }
        binding.imgAddProfileEmoji6.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_SAD))
        }
    }

    private fun createTextView(): View {
        val tv = TextView(requireContext())
        val param = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        param.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        param.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        param.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

        tv.layoutParams = param
        tv.setBackgroundColor(Color.rgb(184, 236, 188))
        tv.id = ViewCompat.generateViewId()

        tv.text = tv.id.toString()
        tv.textSize = 30F
        return tv
    }

    private fun createImageView(emoji: Int): View {
        val img = ImageView(requireContext())
        val param = ConstraintLayout.LayoutParams(70, 70)
        param.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        param.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        param.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

        img.layoutParams = param

        when (emoji) {
            EMOJI_AWW -> Glide.with(requireContext()).load(R.drawable.emoji_aww).into(img)
            EMOJI_CLAP -> Glide.with(requireContext()).load(R.drawable.emoji_clap).into(img)
            EMOJI_DANCE -> Glide.with(requireContext()).load(R.drawable.emoji_dance).into(img)
            EMOJI_HEART -> Glide.with(requireContext()).load(R.drawable.emoji_hearts).into(img)
            EMOJI_PARTY -> Glide.with(requireContext()).load(R.drawable.emoji_party).into(img)
            EMOJI_SAD -> Glide.with(requireContext()).load(R.drawable.emoji_sad).into(img)
        }
        img.id = ViewCompat.generateViewId()
        img.setOnLongClickListener {
            emojiDelete(it)
//            var popupMenu = PopupMenu(context, it)
//            popupMenu.inflate(R.menu.delete_menu)
//            popupMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    // 숨김 친구 리스트로 이동
//                    R.id.menu_delete -> {
//                        // TODO: ViewModel 함수로 삭제할 아이템의 아이디를 전달
//                        Snackbar.make(requireView(), "삭제하겠습니다", Snackbar.LENGTH_SHORT).show()
//                    }
//                    else -> { // 실행되지 않음
//                    }
//                }
//                return@setOnMenuItemClickListener false
//            }
//            popupMenu.show()
//            return@setOnLongClickListener true
        }

        return img
    }

    private fun emojiDelete(item: View): Boolean {
        var popupMenu = PopupMenu(context, item)
        popupMenu.inflate(R.menu.delete_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                // 숨김 친구 리스트로 이동
                R.id.menu_delete -> {
                    // TODO: ViewModel 함수로 삭제할 아이템의 아이디를 전달
                    Snackbar.make(requireView(), "삭제하겠습니다", Snackbar.LENGTH_SHORT).show()
                    binding.cstAddProfile.removeView(item)
                }
                else -> { // 실행되지 않음
                }
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
        return true
    }

    private fun createStickerView(): View {
        val cst = ConstraintLayout(requireContext())
        val img = ConstraintLayout(requireContext())
        val param = ConstraintLayout.LayoutParams(70, 70)
        param.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        param.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        param.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

        cst.layoutParams = param
        Glide.with(requireContext()).load(R.drawable.emoji_aww)
            .into(img.findViewById(R.id.img_sticker_preview))

//        cst.findViewById<ConstraintLayout>(R.id.img_sticker_delete_preview).setOnClickListener {
//            cst.dispatchStartTemporaryDetach()
//        }
        cst.id = ViewCompat.generateViewId()

        return cst
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100

        private const val EMOJI_AWW = 10
        private const val EMOJI_CLAP = 20
        private const val EMOJI_DANCE = 30
        private const val EMOJI_HEART = 40
        private const val EMOJI_PARTY = 50
        private const val EMOJI_SAD = 60
    }
}
