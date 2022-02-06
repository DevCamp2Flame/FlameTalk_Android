package com.sgs.devcamp2.flametalk_android.ui.createopenchatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/02
 */
@HiltViewModel
class CreateOpenChatRoomViewModel @Inject constructor() : ViewModel() {
    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    private val _openChatRoomTitle = MutableStateFlow("")
    val openChatRoomTitle = _openChatRoomTitle.asStateFlow()

    fun setBackgroundImage(path: String) {
        if (path != null) {
            _backgroundImage.value = path
        }
    }

    fun updateTitle(input: String) {
        _openChatRoomTitle.value = input
        Log.d("로그", "$input")
    }

    fun createOpenChatRoom() {
    }
}
