package com.sgs.devcamp2.flametalk_android.domain.entity

/**
 * @author boris
 * @created 2022/01/29
 */
sealed class LocalResults<out T : Any> {

    data class Success<out T : Any>(val data: T) : LocalResults<T>()

    data class Error(val message: String) : LocalResults<Nothing>()
}
