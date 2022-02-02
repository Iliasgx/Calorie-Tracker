package com.iliasg.core.utils

// Base UI Events for navigation.
sealed class UiEvent {
    object Success: UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackBar(val message: UiText) : UiEvent()
}
