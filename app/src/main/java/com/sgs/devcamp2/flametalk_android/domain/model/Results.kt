package com.sgs.devcamp2.flametalk_android.domain.model

/**
 * @author boris
 * @created 2022/01/27
 */
sealed class Results<out T : Any> {
    data class Success<out T : Any>(val data: T) : Results<T>()
}
