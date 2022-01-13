package com.sgs.devcamp2.flametalk_android.ui.friend

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    userRepository: UserRepository
) : ViewModel() {
}