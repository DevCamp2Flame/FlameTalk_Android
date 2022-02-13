package com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/04
 */
class SaveReceivedMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
     fun invoke(chatRes: ChatRes): Flow<Long> {
        return repository.saveReceivedMessage(chatRes)
    }
}
