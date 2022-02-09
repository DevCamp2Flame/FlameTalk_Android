package com.sgs.devcamp2.flametalk_android.ui.updatechatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.GetThumbnailListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.UpdateChatRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/07
 */
@HiltViewModel
class UpdateChatRoomViewModel @Inject constructor(
    private val updateChatRoomUseCase: UpdateChatRoomUseCase,
    private val getThumbnailListUseCase: GetThumbnailListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<UpdateChatRoomRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _lastReadMessageId = MutableStateFlow("")
    val lastReadMessageId = _lastReadMessageId.asStateFlow()
    private val _inputLock = MutableStateFlow(false)
    val inputLock = _inputLock.asStateFlow()
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _imageUrl = MutableStateFlow<String>("")
    val imageUrl = _imageUrl.asStateFlow()

    private val _thumbnailUiState = MutableStateFlow<UiState<ThumbnailWithRoomId>>(UiState.Loading)
    val thumbnailWithRoomId = _thumbnailUiState.asStateFlow()

    lateinit var updateChatRoomReq: UpdateChatRoomReq
    val TAG: String = "로그"

    fun updateChatRoom(userChatRoomId: Long, thumbnailList: List<Thumbnail>) {
        if (thumbnailList.size == 1) {
            _imageUrl.value = thumbnailList[0].image
        }
        if (_imageUrl.value == "") {
            updateChatRoomReq = UpdateChatRoomReq(_inputLock.value, _title.value, null)
        } else {
            updateChatRoomReq = UpdateChatRoomReq(_inputLock.value, _title.value, _imageUrl.value)
        }

        viewModelScope.launch {
            updateChatRoomUseCase.invoke(userChatRoomId, updateChatRoomReq)
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {
                                _uiState.value = UiState.Success(result.data)
                            }
                    }
                }
        }
    }
    fun getThumbnailList(chatroomId: String) {
        viewModelScope.launch {
            getThumbnailListUseCase.invoke(chatroomId).collect {
                result ->
                when (result) {
                    is LocalResults.Success ->
                        {
                            _thumbnailUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
    fun updateInputLock() {
        _inputLock.value = !_inputLock.value
        Log.d(TAG, "input lock value - ${_inputLock.value}")
    }
    fun updateTitle(input: String) {
        _title.value = input
    }
    fun setBackgroundImage(path: String) {
        if (path != null) {

            _imageUrl.value = path
        }
    }
}
