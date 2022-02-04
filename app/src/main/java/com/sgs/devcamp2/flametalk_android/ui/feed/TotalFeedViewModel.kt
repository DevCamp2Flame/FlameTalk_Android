package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {

    // 프로필 이미지
    private val _profileImage: MutableStateFlow<String>? = null
    val profileImage = _profileImage?.asStateFlow()

    // 피드 리스트
    private val _totalFeed: MutableLiveData<List<Feed>> = MutableLiveData()
    val totalFeed: MutableLiveData<List<Feed>> = _totalFeed

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

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
}
