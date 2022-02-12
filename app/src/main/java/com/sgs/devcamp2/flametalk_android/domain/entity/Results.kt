package com.sgs.devcamp2.flametalk_android.domain.entity

/**
 * @author boris
 * @created 2022/01/27
 * remote api 의 결과를 wrapping 함
 */
sealed class Results<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : Results<T, Nothing>()
    data class Error<U : Any>(val message: String) : Results<Nothing, U>()
    object Loading : Results<Nothing, Nothing>()
}
