package com.backpacker.rstpstream.domain.model

sealed class StreamState {
    object Loading : StreamState()
    object Playing : StreamState()
    object Stopped : StreamState()
    data class Error(val message: String) : StreamState()
}