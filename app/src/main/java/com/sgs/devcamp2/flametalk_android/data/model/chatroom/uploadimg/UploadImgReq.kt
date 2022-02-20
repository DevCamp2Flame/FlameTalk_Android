package com.sgs.devcamp2.flametalk_android.data.model.chatroom.uploadimg

import java.io.File

/**
 * @author boris
 * @created 2022/02/14
 */
data class UploadImgReq(
    val file: File,
    val chatroomId: String
)
