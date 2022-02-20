package com.sgs.devcamp2.flametalk_android.ui.updatechatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.uploadimg.UploadImgRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.GetThumbnailListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.UpLoadImageUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.UpdateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.util.pathToMultipartImageFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/07
 */
@HiltViewModel
class UpdateChatRoomViewModel @Inject constructor(
    private val updateChatRoomUseCase: UpdateChatRoomUseCase,
    private val getThumbnailListUseCase: GetThumbnailListUseCase,
    private val upLoadImageUseCase: UpLoadImageUseCase,
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

    private val _url = MutableStateFlow("")
    val url = _url.asStateFlow()

    val _userChatroomId = MutableStateFlow(0L)

    val _thumbnailList = MutableStateFlow<List<Thumbnail>>(emptyList())

    private val _thumbnailUiState = MutableStateFlow<UiState<ThumbnailWithRoomId>>(UiState.Loading)
    val thumbnailWithRoomId = _thumbnailUiState.asStateFlow()

    private val _imageUpLoadState = MutableStateFlow<UiState<UploadImgRes>>(UiState.Loading)
    val imageUploadState = _imageUpLoadState.asStateFlow()

    lateinit var updateChatRoomReq: UpdateChatRoomReq
    val TAG: String = "로그"

    fun updateChatRoom(chatroomId: String, userChatRoomId: Long) {
        if (_url.value != "") {
            updateChatRoomReq = UpdateChatRoomReq(_inputLock.value, _title.value, _url.value)
        } else if (_thumbnailList.value.size == 1) {
            updateChatRoomReq = UpdateChatRoomReq(_inputLock.value, _title.value, _thumbnailList.value[0].image)
        } else {
            updateChatRoomReq = UpdateChatRoomReq(_inputLock.value, _title.value, null)
        }
        viewModelScope.launch {
            updateChatRoomUseCase.invoke(chatroomId, userChatRoomId, updateChatRoomReq)
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

    /**
     * local에서 썸네일 리스트를 가져온다.
     */
    fun getThumbnailList(chatroomId: String) {
        viewModelScope.launch {
            getThumbnailListUseCase.invoke(chatroomId).collect {
                result ->
                when (result) {
                    is LocalResults.Success ->
                        {
                            _thumbnailUiState.value = UiState.Success(result.data)
                            _thumbnailList.value = result.data.thumbnailList
                            _userChatroomId.value = result.data.room.userChatroomId
                        }
                }
            }
        }
    }
    fun updateInputLock() {
        _inputLock.value = !_inputLock.value
    }
    fun updateTitle(input: String) {
        _title.value = input
    }
    fun setBackgroundImage(path: String) {
        if (path != null) {
            _imageUrl.value = path
        }
    }
    fun updateImageUrl(url: String) {
        _url.value = url
    }

    fun updateImage() {
        viewModelScope.launch {
            val multipartFile: MultipartBody.Part? = pathToMultipartImageFile(_imageUrl.value, "image/*".toMediaTypeOrNull())
            upLoadImageUseCase.invoke(multipartFile!!, null).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _imageUpLoadState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
}
