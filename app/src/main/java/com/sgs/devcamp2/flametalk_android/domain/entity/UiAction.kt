package com.sgs.devcamp2.flametalk_android.domain.entity

/**
 * @author 김현국
 * @created 2022/01/29
 */
sealed class UiAction<out T : Any> {
    class Click<out T : Any>(val data: T) : UiAction<T>()
    class Stop<out T : Any>(val data: T) : UiAction<T>()
}
