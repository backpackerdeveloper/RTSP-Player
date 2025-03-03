package com.backpacker.rstpstream.viewmodel

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.backpacker.rstpstream.domain.model.StreamState
import com.backpacker.rstpstream.utils.ExoPlayerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers

class StreamViewModel : ViewModel() {

    var exoPlayer: ExoPlayer? = null
    private val _streamState = MutableStateFlow<StreamState>(StreamState.Stopped)
    val streamState: StateFlow<StreamState> = _streamState

    fun initializePlayer(context: Context) {
        exoPlayer = ExoPlayerHelper.getExoPlayerInstance(context)
    }

    @OptIn(UnstableApi::class)
    fun startStreaming(url: String) {
        if (url.isEmpty()) {
            _streamState.value = StreamState.Error("Please enter a valid RTSP URL")
            return
        }
        _streamState.value = StreamState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mediaItem = ExoPlayerHelper.createMediaItem(Uri.parse(url))
                val mediaSource = ExoPlayerHelper.createRtspMediaSource(mediaItem)

                exoPlayer?.apply {
                    setMediaSource(mediaSource)
                    prepare()
                    playWhenReady = true
                }

                _streamState.value = StreamState.Playing
            } catch (e: Exception) {
                _streamState.value = StreamState.Error("Streaming error: ${e.message}")
            }
        }
    }

    fun pauseStreaming() {
        exoPlayer?.pause()
        _streamState.value = StreamState.Stopped
    }

    fun stopStreaming() {
        exoPlayer?.stop()
        _streamState.value = StreamState.Stopped
    }

    override fun onCleared() {
        ExoPlayerHelper.releaseExoPlayer()
        super.onCleared()
    }
}
