package com.sgs.devcamp2.flametalk_android.util

import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

// file local path -> MultipartBody.Part
fun pathToMultipartImageFile(url: String, mediaType: MediaType?): MultipartBody.Part {
    val file = File(url)
    val requestFile = file.asRequestBody(mediaType)

    return MultipartBody.Part.createFormData(
        "file", file.name, requestFile
    )
} // Example: pathToMultipartImageFile(_profileImage.value, "image/*".toMediaTypeOrNull())
