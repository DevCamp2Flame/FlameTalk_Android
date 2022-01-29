package com.sgs.devcamp2.flametalk_android.domain.entity

/**
 * @author boris
 * @created 2022/01/27
 */
sealed class Results<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : Results<T, Nothing>()
    data class Error<U : Any>(val message: String) : Results<Nothing, U>()
}
