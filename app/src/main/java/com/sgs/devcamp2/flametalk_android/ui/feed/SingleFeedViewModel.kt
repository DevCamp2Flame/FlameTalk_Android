package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.response.feed.Feed
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SingleFeedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {

    // 프로필 이미지
    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    // 피드 리스트
    private val _feeds: MutableStateFlow<List<Feed>?> = MutableStateFlow(null)
    val feeds: MutableStateFlow<List<Feed>?> = _feeds

    // 피드 재호출할지 여부
    private val _reload = MutableStateFlow(false)
    val reload = _reload.asStateFlow()

    // 피드 재호출할지 여부
    private val _lockChanged: MutableStateFlow<Boolean>? = null
    val lockChanged = _lockChanged?.asStateFlow()

    private val _message: MutableStateFlow<String>? = null
    val message = _message?.asStateFlow()

    // 현재 보고있는 image feed position
    private val _currentPosition = MutableStateFlow(1)
    val currentPosition = _currentPosition.asStateFlow()

    private val _error: MutableStateFlow<String>? = null
    val error = _error?.asStateFlow()

    fun getSingleFeedList(profileId: Long, isBackground: Boolean) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getSingleFeedList(profileId, isBackground)
                if (response.data.profileImage != null) {
                    _profileImage.value = response.data.profileImage
                }
                _feeds.value = response.data.feeds

                Timber.d("$response")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    fun deleteFeed() {
        viewModelScope.launch {
            try {
                val item = feeds.value?.get(_currentPosition.value)
                val response = profileRepository.get().deleteFeed(item!!.feedId)
                _message?.value = response.message

                // 피드 아이템이 삭제되면 리스트를 재호출한다
                if (response.status == 200) {
                    _reload.value = true
                } else {
                    _reload.value = false
                    _message?.value = response.message
                }

                _reload.value = false
                Timber.d("$response")
                Timber.d("아이템 삭제 요청 ${item.feedId}")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    fun updateFeedImageLock() {
        viewModelScope.launch {
            try {
                val item = feeds.value?.get(_currentPosition.value)
                val response = profileRepository.get().updateFeedImageLock(item!!.feedId)

                if (response.status == 200) {
                    _lockChanged?.value = true
                    _reload.value = true
                } else {
                    _reload.value = false
                    _message?.value = response.message
                }

                Timber.d("$response")
                Timber.d("아이템 숨김여부 변경 요청 ${item.feedId}")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    fun currentImagePosition(position: Int) {
        _currentPosition.value = position
    }
}
