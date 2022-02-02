package com.sgs.devcamp2.flametalk_android.ui.friend.phonebook

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class PhoneBookViewModel @Inject constructor(
    @ApplicationContext private val context: Context,

) : ViewModel() {
    // 주소록 전화번호
    private val _contact: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val contact: MutableLiveData<ArrayList<String>> = _contact

    init {
    }

    fun getContacts(data: ArrayList<String>) {
        // TODO: ListData 역할을 하는 StateFlow 객체에  dummyData를 담고 View에서 Observe하자
        Timber.d("contact %s", data)
        _contact.value = data
    }
}
