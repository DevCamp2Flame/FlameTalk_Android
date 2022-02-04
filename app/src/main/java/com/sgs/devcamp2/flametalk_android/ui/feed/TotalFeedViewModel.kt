package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyHorizentalFeed
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
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
    private val signRepository: Lazy<SignRepository>
) : ViewModel() {

    // 프로필 이미지
    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    // 피드 리스트
    private val _totalFeed: MutableLiveData<List<Feed>> = MutableLiveData()
    val totalFeed: MutableLiveData<List<Feed>> = _totalFeed

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        _profileImage.value = getDummyHorizentalFeed().data.profileImage
        _totalFeed.value = getDummyHorizentalFeed().data.feeds
    }

    fun getFeedData(email: String, password: String, social: String, deviceId: String) {
        viewModelScope.launch {
            val request = SigninRequest(
                email, password, social, deviceId
            )
            val response = signRepository.get().signin(request)
            // _nickname.value = response.nickname
            Timber.d("Signin Response: $response")
//            try {
//                val request = SigninRequest(
//                    email, password, social, deviceId
//                )
//                val response = signRepository.get().signin(request)
//                // _nickname.value = response.nickname
//                Timber.d("Signin Response: $response")
//            } catch (ignored: Throwable) {
//                // _error.value = "알 수 없는 에러 발생"
//                Timber.d("Signin Response error: $_error")
//            }
        }
    }
}
