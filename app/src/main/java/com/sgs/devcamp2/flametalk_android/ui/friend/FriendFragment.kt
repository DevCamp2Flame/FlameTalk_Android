package com.sgs.devcamp2.flametalk_android.ui.friend

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentFriendBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.birthday.BirthdayAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.friends.FriendAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.multi_profile.MultiProfileAdapter
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/17
 * @updated 2022/01/31
 * @desc 프로필 상세 보기 (유저, 친구)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FriendFragment : Fragment() {
    private val binding by lazy { FragmentFriendBinding.inflate(layoutInflater) }
    private val viewModel: FriendViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val multiProfileAdapter: MultiProfileAdapter by lazy {
        MultiProfileAdapter(requireContext())
    }
    private val birthdayAdapter: BirthdayAdapter by lazy {
        BirthdayAdapter(requireContext())
    }
    private val friendAdapter: FriendAdapter by lazy {
        FriendAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initAppbar()
        initUserProfiles()
    }

    private fun initAppbar() {
        binding.abFriend.tvAppbar.text = "친구"
        binding.abFriend.imgAppbarSearch.setOnClickListener {
            // TODO: Friend > Search
        }
        binding.abFriend.imgAppbarAddFriend.setOnClickListener {
            findNavController().navigate(R.id.navigation_add_friend)
        }
        binding.abFriend.imgAppbarSetting.setOnClickListener {
            var popupMenu = PopupMenu(context, binding.abFriend.imgAppbarSetting)
            popupMenu.inflate(R.menu.friend_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 숨김 친구 리스트로 이동
                    R.id.menu_hide -> {
                        findNavController().navigate(R.id.navigation_hidden_friend)
                    }
                    // 차단 친구 리스트로 이동
                    R.id.menu_block -> {
                        findNavController().navigate(R.id.navigation_blocked_friend)
                    }
                    else -> {
                        // 실행되지 않음
                        Snackbar.make(
                            binding.abFriend.imgAppbarSetting,
                            "else...",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }

        // 연락처 동기화하여 친구 추가
        binding.tvFriendLoadContact.setOnClickListener {
            loadFriends()
        }
    }

    // 프로필 초기화
    private fun initUserProfiles() {
        initMainProfile()
        initMultiProfile()

        // 생일, 친구 프로필
        initBirthdayProfile()
        initFriendProfile()
    }

    // 유저프로필 초기화
    // 유저 닉네임은 list 데이터로 넘어오지 않기 때문에 따로 observe
    private fun initMainProfile() {
        // preference에서 가져온 사용자 닉네임
        lifecycleScope.launch {
            viewModel.nickname.collectLatest {
                if (it.isNotEmpty()) {
                    multiProfileAdapter.nickname = it
                    binding.lFriendMainUser.tvFriendPreviewNickname.text = it
                }
            }
        }

        // 사용자 프로필
        lifecycleScope.launchWhenResumed {
            viewModel.userProfile.collectLatest {
                Glide.with(binding.lFriendMainUser.imgFriendPreview)
                    .load(it?.imageUrl).apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.lFriendMainUser.imgFriendPreview)

                if (it?.description.isNullOrEmpty()) {
                    binding.lFriendMainUser.tvFriendPreviewDesc.toVisibleGone()
                } else {
                    binding.lFriendMainUser.tvFriendPreviewDesc.toVisible()
                    binding.lFriendMainUser.tvFriendPreviewDesc.text = it?.description
                    Timber.d("description ${it?.description}")
                }
            }
        }

        // 내 프로필 미리보기 > 프로필 상세 보기 이동
        binding.lFriendMainUser.root.setOnClickListener {
            val friendToProfileDirections: NavDirections =
                FriendFragmentDirections.actionFriendToProfile(
                    viewType = USER_DEFAULT_PROFILE, profileId = viewModel.userProfile.value!!.id
                )
            findNavController().navigate(friendToProfileDirections)
        }
    }

    // 멀티프로필 리스트 초기화
    private fun initMultiProfile() {
        binding.rvFriendMultiProfile.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvFriendMultiProfile.adapter = multiProfileAdapter

        lifecycleScope.launchWhenResumed {
            viewModel.multiProfile.collectLatest {
                if (it.isNotEmpty()) {
                    multiProfileAdapter.data = it
                    multiProfileAdapter.notifyDataSetChanged()
                }
            }
        }
        // 마지막 아이템: 만들기
        binding.itemFriendAddProfile.imgVerticalProfileNone.toVisible()
        binding.itemFriendAddProfile.tvVerticalProfileNickname.text = "만들기"

        // 친구리스트 > 멀티프로필 생성: 멀티 프로필 만들기
        binding.itemFriendAddProfile.root.setOnClickListener {
            Snackbar.make(requireView(), "멀티프로필 생성 클릭", Snackbar.LENGTH_SHORT).show()
            Timber.d("멀티프로필 생성 클릭")
            findNavController().navigate(R.id.navigation_add_profile)
        }
    }

    // 생일인 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvFriendBirthday.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriendBirthday.adapter = birthdayAdapter

        lifecycleScope.launch {
            viewModel.birthProfile.collectLatest {
                if (it.isNullOrEmpty()) {
                    birthdayVisibility(GONE)
                } else {
                    birthdayAdapter.data = it
                    birthdayAdapter.notifyDataSetChanged()
                    birthdayVisibility(VISIBLE)
                }
            }
        }
        // 마지막 아이템: 친구의 생일을 확인해보세요
        binding.itemFriendMoreBirthday.imgFriendPreviewNone.toVisible()
        binding.itemFriendMoreBirthday.tvFriendPreviewCount.toVisible()
        binding.itemFriendMoreBirthday.tvFriendPreviewDesc.toVisibleGone()
        binding.itemFriendMoreBirthday.tvFriendPreviewCount.text = "n"
    }

    // 주소록 친구 리스트 초기화
    private fun initFriendProfile() {
        binding.rvFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriend.adapter = friendAdapter
        lifecycleScope.launch {
            viewModel.friendProfile.collectLatest {
                if (it.isNullOrEmpty()) {
                    friendVisibility(GONE)
                } else {
                    friendAdapter.data = it
                    friendAdapter.notifyDataSetChanged()
                    friendVisibility(VISIBLE)
                }
            }
        }
    }

    // 생일 친구 리스트 노출 여부
    private fun birthdayVisibility(visibleType: Int) {
        binding.rvFriendBirthday.visibility = visibleType
        binding.itemFriendMoreBirthday.root.visibility = visibleType
        binding.tvFriendBirthday.visibility = visibleType
    }

    // 친구 리스트 노출 여부
    private fun friendVisibility(visibleType: Int) {
        binding.rvFriend.visibility = visibleType
        binding.tvFriendCount.visibility = visibleType

        when (visibleType) {
            GONE -> binding.tvFriendLoadContact.toVisible()
            VISIBLE -> binding.tvFriendLoadContact.toVisibleGone()
        }
    }

    private fun loadFriends() {
        if (checkPermission()) {
            // 권한이 있으면 연락처 리스트를 불러온다
            // viewModel.getContactList()
            viewModel.getContactsList()
        } else {
            Snackbar.make(
                requireView(), "권한이 없으면 연락처 동기화로 친구추가할 수 없습니다.", Snackbar.LENGTH_SHORT
            ).show()
        }
        // TODO: 연락처에서 친구 리스트 load
        // TODO: 친구 추가 통신 요청하는 뷰모델 함수 호출
    }

    // 주소록 접근 권한 확인
    private fun checkPermission(): Boolean {
        val status = ContextCompat.checkSelfPermission(
            requireContext(),
            "android.permission.READ_CONTACTS"
        )
        // 권한 확인
        if (status == PackageManager.PERMISSION_GRANTED) {
            return true
        } else { // 권한 요청 다이얼로그
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf("android.permission.READ_CONTACTS"),
                100
            )
            Timber.d("Contacts Permission Denied")
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.d("Contacts Permission Grants")
        } else {
            Timber.d("Contacts Permission Denied")
        }
    }

    companion object {
        const val USER_DEFAULT_PROFILE = 1
        const val BIRTHDAY_FRIEND = 100
        const val FRIEND = 200

        const val GONE = 8
        const val VISIBLE = 0
    }
}
