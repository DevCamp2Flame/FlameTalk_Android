package com.sgs.devcamp2.flametalk_android.util

import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc 파일 관련 확장함수
 */

// 파일을 Multipart Form Data로 변환 (file local path -> MultipartBody.Part)
fun pathToMultipartImageFile(url: String, mediaType: MediaType?): MultipartBody.Part {
    val file = File(url)
    val requestFile = file.asRequestBody(mediaType)

    return MultipartBody.Part.createFormData(
        "file", file.name, requestFile
    )
} // Example: pathToMultipartImageFile(_profileImage.value, "image/*".toMediaTypeOrNull())
