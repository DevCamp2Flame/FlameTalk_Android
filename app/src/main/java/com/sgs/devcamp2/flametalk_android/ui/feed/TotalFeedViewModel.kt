package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
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
class TotalFeedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signRepository: Lazy<SignRepository>,
    private val fileRepository: Lazy<FileRepository>,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {

    // 프로필 이미지
    private val _profileImage: MutableStateFlow<String>? = null
    val profileImage = _profileImage?.asStateFlow()

    // 피드 리스트
    private val _totalFeed: MutableLiveData<List<Feed>> = MutableLiveData()
    val totalFeed: MutableLiveData<List<Feed>> = _totalFeed

    // 피드 재호출할지 여부
    private val _reload = MutableStateFlow(false)
    val reload = _reload?.asStateFlow()

    private val _message: MutableStateFlow<String>? = null
    val message = _message?.asStateFlow()

    private val _error: MutableStateFlow<String>? = null
    val error = _error?.asStateFlow()

    fun getTotalFeedList(profileId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getTotalFeedList(profileId)
                _totalFeed.value = response.data.feeds

                Timber.d("$response")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    // TODO: file 다운로드 요청
    fun downloadItem(fileId: String) {
        viewModelScope.launch {
            try {
                // val response = fileRepository.get().downloadItem(fileId)
                // Timber.d("$response")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    fun deleteFeed(feedId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().deleteFeed(feedId)
                _message?.value = response.message
                if (response.status == 200) {
                    _reload?.value = true
                } else {
                    _message?.value = response.message
                }
                _reload?.value = false
                Timber.d("$response")
                Timber.d("아이템 삭제 요청 $feedId")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }

    fun updateFeedImageLock(feedId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().updateFeedImageLock(feedId)
                Timber.d("$response")
                Timber.d("로그 확인 ${response.status} ${response.message}")
                Timber.d("아이템 숨김여부 변경 요청 $feedId")
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Error:  $ignored")
            }
        }
    }
}
