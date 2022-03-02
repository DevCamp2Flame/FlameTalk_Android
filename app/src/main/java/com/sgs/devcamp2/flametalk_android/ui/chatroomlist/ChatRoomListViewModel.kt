package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenReq
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.DeleteChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetLocalChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveDeviceTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * @author 김현국
 * @created 2022/01/26
 */
@HiltViewModel
class ChatRoomListViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val deleteChatRoomUseCase: DeleteChatRoomUseCase,
    private val getLocalChatRoomListUseCase: GetLocalChatRoomListUseCase,
    private val saveDeviceTokenUseCase: SaveDeviceTokenUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private val _uiState = MutableStateFlow<UiState<GetChatRoomListRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _localUiState = MutableStateFlow<UiState<List<ThumbnailWithRoomId>>>(UiState.Loading)
    val localUiState = _localUiState.asStateFlow()

    private val _deviceToken = MutableStateFlow<UiState<String>>(UiState.Loading)
    val deviceToken = _deviceToken.asStateFlow()

    private val _deviceTokenUiState = MutableStateFlow<UiState<SaveDeviceTokenRes>>(UiState.Loading)
    val deviceTokenUiState = _deviceTokenUiState.asStateFlow()

    /**
     * 채팅방 리스트를 서버에서 가져오는 function입니다.
     * @param isOpen 오픈 채팅 유무
     */
    fun getChatRoomList(isOpen: Boolean) {
        viewModelScope.launch {
            getChatRoomListUseCase.invoke(isOpen)
                .collect { result ->
                    when (result) {
                        is Results.Success -> {
                            _uiState.value = UiState.Success(result.data)
                        }
                    }
                }
        }
    }
    /**
     * 로컬 Room Database에서 채팅방 리스트를 가져오는 function입니다.
     * @param isOpen 오픈 채팅 유무
     */
    fun getLocalChatRoomList(isOpen: Boolean) {
        viewModelScope.launch {
            getLocalChatRoomListUseCase.invoke(isOpen)
                .collect {
                    result ->
                    when (result) {
                        is LocalResults.Success ->
                            {
                                _localUiState.value = UiState.Success(result.data)
                            }
                    }
                }
        }
    }
    /**
     * 채팅방 삭제 fucntion입니다.
     * @param userChatroomId 유저의 채팅방 id
     */

    fun deleteChatRoom(userChatroomId: Long) {
        viewModelScope.launch {
            deleteChatRoomUseCase.invoke(userChatroomId).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            //    _deleteUiState.value = UiState.Success(true)
                        }
                }
            }
        }
    }
    /**
     * fcm device token 저장 function입니다.
     */
    fun saveDeviceToken(deviceToken: String) {
        viewModelScope.launch {
            val saveDeviceTokenReq = SaveDeviceTokenReq(deviceToken)
            Log.d(TAG, "saveDeviceTokenReq - $saveDeviceTokenReq")
            saveDeviceTokenUseCase.invoke(saveDeviceTokenReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _deviceTokenUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
    fun getDeviceToken(context: Context) {
        val pref = context.getSharedPreferences("deviceToken", Context.MODE_PRIVATE)
        var token = pref.getString("deviceToken", "")
        _deviceToken.value = UiState.Success(token.toString())
    }
}
