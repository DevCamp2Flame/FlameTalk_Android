package com.sgs.devcamp2.flametalk_android.ui.chatroom

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.domain.FakeChatRoomRepository
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author boris
 * @created 2022/03/02
 */
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ChatRoomViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val coroutineDispatcher = StandardTestDispatcher()

    lateinit var getChatRoomListUseCase: GetChatRoomListUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(coroutineDispatcher)
        val fakeChatRoomRepository = FakeChatRoomRepository()
        getChatRoomListUseCase = GetChatRoomListUseCase(fakeChatRoomRepository)
    }

    @Test
    fun getChatRoomList(): Unit = runTest {
        launch(Dispatchers.Main) {
            val userChatRoom = UserChatRoom("1", 1, "test", null, "123", false, 2)
            val userChatRooms = listOf<UserChatRoom>(userChatRoom)
            val getChatRoomListRes = GetChatRoomListRes("1643163512893324414", userChatRooms)

            getChatRoomListUseCase.invoke(false)
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {
                                Assert.assertEquals(getChatRoomListRes, result.data)
                            }
                    }
                }
        }
    }
}
