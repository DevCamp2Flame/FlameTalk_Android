package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.uploadimg.UploadImgRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/14
 */
class UpLoadImageUseCase @Inject constructor(
    val repository: ChatRoomRepository
) {
    fun invoke(file: MultipartBody.Part,  chatroomId: RequestBody?): Flow<Results<UploadImgRes, WrappedResponse<UploadImgRes>>> {
        return repository.uploadImage(file, chatroomId)
    }
}
