package com.sgs.devcamp2.flametalk_android.ui.createopenchatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.GetOpenProfileListRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenchatprofile.GetOpenProfileListUseCase
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/02
 */
@HiltViewModel
class CreateOpenChatRoomViewModel @Inject constructor(
    private val createChatRoomUseCase: CreateChatRoomUseCase,
    private val getOpenProfileListUseCase: GetOpenProfileListUseCase,
    private val userDAO: UserDAO
    // get open profile usecase
) : ViewModel() {

    private var _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()
    private var _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()
    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    private val _openChatRoomTitle = MutableStateFlow("")
    val openChatRoomTitle = _openChatRoomTitle.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _openProfileList = MutableStateFlow<UiState<GetOpenProfileListRes>>(UiState.Loading)
    val openProfileList = _openProfileList.asStateFlow()

    private val _createdOpenChatRoomUiState = MutableStateFlow<UiState<ChatRoomEntity>>(UiState.Loading)
    val createOpenCHatRoomUiState = _createdOpenChatRoomUiState.asStateFlow()

    lateinit var _openProfile: OpenProfile
    init {
        viewModelScope.launch {
            userDAO.user.collect {
                if (it != null) {
                    _userId.value = it.userId
                    _nickname.value = it.nickname
                }
            }
        }
    }

    fun setBackgroundImage(path: String) {
        if (path != null) {
            _backgroundImage.value = path
        }
    }

    fun updateTitle(input: String) {
        _openChatRoomTitle.value = input
        Log.d("로그", "$input")
    }
    /**
     * 저의 모든 오픈 프로필을 가져오는 function입니다.
     */
    fun getOpenProfileList() {
        viewModelScope.launch {
            getOpenProfileListUseCase.invoke().collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _openProfileList.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }

    /**
     * 선택한 오픈프로필로 오픈 채팅방을 생성하는 function입니다.
     */
    fun createOpenChatRoom() {
        val createChatRoomReq = CreateChatRoomReq(_userId.value, _openProfile.openProfileId, true, emptyList(), _openChatRoomTitle.value, _backgroundImage.value)
        viewModelScope.launch {
            createChatRoomUseCase.invoke(createChatRoomReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _createdOpenChatRoomUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }

    fun updateSelectedOpenProfile(openProfile: OpenProfile) {
        _openProfile = openProfile
    }
}
