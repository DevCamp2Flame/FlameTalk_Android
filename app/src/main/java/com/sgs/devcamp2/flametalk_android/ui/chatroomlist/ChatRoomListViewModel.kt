package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.DeleteChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetLocalChatRoomListUseCase
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
) : ViewModel() {
    val TAG: String = "로그"
    private val _uiState = MutableStateFlow<UiState<GetChatRoomListRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _localUiState = MutableStateFlow<UiState<List<ThumbnailWithRoomId>>>(UiState.Loading)
    val localUiState = _localUiState.asStateFlow()
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
}
