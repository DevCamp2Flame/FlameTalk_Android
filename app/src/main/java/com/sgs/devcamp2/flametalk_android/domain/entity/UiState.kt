package com.sgs.devcamp2.flametalk_android.domain.entity

/**
 * @author boris
 * @created 2022/01/27
 */
sealed class UiState<out T : Any> {

    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Init : UiState<Nothing>()
}
