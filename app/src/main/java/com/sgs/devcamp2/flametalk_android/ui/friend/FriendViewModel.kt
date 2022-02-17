package com.sgs.devcamp2.flametalk_android.ui.friend

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview
import com.sgs.devcamp2.flametalk_android.domain.entity.FriendModel
import com.sgs.devcamp2.flametalk_android.domain.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.friend.AddContactFriendRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FriendViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: Lazy<ProfileRepository>,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

    // 유저 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 유저 아이디
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 메인 유저 정보
    private val _userProfile: MutableStateFlow<ProfilePreview?> = MutableStateFlow(null)
    val userProfile = _userProfile?.asStateFlow()

    // 유저 멀티프로필
    private val _multiProfile = MutableStateFlow<List<ProfilePreview>?>(null) // (emptyList())
    val multiProfile: MutableStateFlow<List<ProfilePreview>?> = _multiProfile

    // 생일인 친구 리스트
    private val _birthProfile = MutableStateFlow<List<Friend>>(emptyList())
    val birthProfile: MutableStateFlow<List<Friend>> = _birthProfile

    // 친구 리스트
    private val _friendProfile: MutableStateFlow<List<Friend>?> =
        MutableStateFlow(null) // (emptyList())
    val friendProfile: MutableStateFlow<List<Friend>?> = _friendProfile

    // 주소록 전화번호
    private val _contact = MutableStateFlow<List<String>>(emptyList())
    val contact: MutableStateFlow<List<String>> = _contact

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        getProfileList()
        getFriendList(BIRTHDAY_FRIEND)
        getFriendList(FRIEND)
        // _birthProfile.value = dummyBirthdayData
        // _friendProfile.value = dummyFriendData
    }

    private fun getProfileList() {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfileList()
                val multiProfile = arrayListOf<ProfilePreview>() // = ArrayList<ProfilePreview>()

                if (response.status == 200) {
                    _nickname.value = response.data.nickname
                    _userId.value = response.data.userId
                    response.data.profiles.map {
                        when (it.isDefault) {
                            // 기본 프로필
                            true -> _userProfile.value = it
                            // 추가로 생성한 멀티 프로필
                            false -> multiProfile.add(it)
                        }
                    }

                    if (multiProfile.isEmpty()) {
                        _multiProfile.value = null
                    } else {
                        _multiProfile.value = multiProfile
                    }
                    // _multiProfile.value = multiProfile.toList()

                    // Result
//                    Timber.d("User Profile ${_userProfile.value}")
//                    Timber.d("Multi Profile ${_multiProfile.value}")
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    private fun getFriendList(type: Int) {
        viewModelScope.launch {
            try {
                val response = when (type) {
                    BIRTHDAY_FRIEND -> friendRepository.get().getFriendList(true, false, false)
                    FRIEND -> friendRepository.get().getFriendList(null, false, false)
                    else -> null
                }

                if (response?.status == 200) {
                    when (type) {
                        BIRTHDAY_FRIEND -> _birthProfile.value = response.data
                        FRIEND -> {
                            _friendProfile.value = response.data
                            saveFriendProfiles()
                        }
                    }

                    // Result
//                    Timber.d("Birthday Response ${response.data}")
                    Timber.d("Get Friend ${_friendProfile.value}")
                } else {
                    _message.value = response!!.message
                }
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    // 연락처 가져오기
    fun getContactsList() {
        viewModelScope.launch {
            val result = viewModelScope.async {
                var arrayList = ArrayList<String>()
                // 연락처 고유 url
                val uri = ContactsContract.Contacts.CONTENT_URI
                // 오름차순으로 이름 정렬
                val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                // 연락처 고유 url을 기반으로 정보를 쿼리
                val cursor: Cursor? = context.contentResolver.query(
                    uri, null, null, null, sort
                )
                if (cursor != null) {
                    if (cursor.count > 0) {
                        // 데이터가 있으면 id, 이름, content uri를 가져옴
                        while (cursor.moveToNext()) {
                            @SuppressLint("Range") val id = cursor.getString(
                                cursor.getColumnIndex(
                                    ContactsContract.Contacts._ID
                                )
                            )
                            @SuppressLint("Range") val name = cursor.getString(
                                cursor.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME
                                )
                            )
                            val uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            val selection =
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                            val phoneCursor: Cursor? = context.contentResolver.query(
                                uriPhone, null, selection, arrayOf(id), null
                            )
                            // content uri를 추가로 쿼리하여 휴대폰 번호를 가져온다
                            if (phoneCursor!!.moveToNext()) {
                                @SuppressLint("Range") val number = phoneCursor.getString(
                                    phoneCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
                                arrayList.add(number)
                                phoneCursor.close()
                            }
                        }
                        cursor.close()
                    }
                }
                // 가져온 연락처 리스트를 viewModel의 변수에 저장한다
                _contact.value = arrayList
            }
            /**
             * deferred type의 위 작업이 끝날때까지 기다린 후 연락처 기반 친구 추가 통신 요청을 보낸다
             * */
            result.await()
            addContactFriend()
        }
    }

    // 연락처 기반 친구 추가
    private fun addContactFriend() {
        viewModelScope.launch {
            try {
                val request = AddContactFriendRequest(_contact.value)
                val response = friendRepository.get().postContactFriendAdd(request)

                if (response.status == 201) {
                    _message.value = response.message
                    // 연락처 친구 추가가 성공하면 친구 목록을 다시 받아온다
                    getFriendList(BIRTHDAY_FRIEND)
                    getFriendList(FRIEND)
                    Timber.d("Success Response: $response")
                }
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    // 친구 프로필 리스트 로컬에 저장
    private fun saveFriendProfiles() {
        viewModelScope.launch {
            var friendsData: List<FriendModel>
            if (_friendProfile.value != null) {
                friendsData = _friendProfile.value!!.map {
                    FriendModel(
                        profileId = it.preview.profileId,
                        nickname = it.nickname,
                        imageUrl = it.preview.imageUrl,
                        description = it.preview.description
                    )
                }
            } else {
                friendsData = emptyList()
            }
            friendRepository.get().insertAllFriends(friendsData)
        }
    }

    companion object {
        const val BIRTHDAY_FRIEND = 100
        const val FRIEND = 200
    }
}
