package com.sgs.devcamp2.flametalk_android.domain.entity

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

/**
 * @author boris
 * @created 2022/01/29
 */

sealed class UiEvent<out T : Any> {

    data class Success<out T : Any>(val data: T) : UiEvent<T>()

    data class Next(@LayoutRes val screen: Int = -1) : UiEvent<Nothing>()

    data class Error(val message: String = "") : UiEvent<Nothing>()

    data class Toast(@StringRes val stringId: Int) : UiEvent<Nothing>()
}
