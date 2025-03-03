package com.backpacker.rstpstream.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.rtsp.RtspMediaSource

@OptIn(UnstableApi::class)
object ExoPlayerHelper {

    @Volatile
    private var exoPlayer: ExoPlayer? = null

    fun getExoPlayerInstance(context: Context): ExoPlayer {
        return exoPlayer ?: synchronized(this) {
            exoPlayer ?: createPlayer(context).also { exoPlayer = it }
        }
    }

    private fun createPlayer(context: Context): ExoPlayer {
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                200,  // Min buffer before playing
                500,  // Max buffer duration
                100,  // Buffer before re-buffering starts
                100   // Buffer after seeking
            )
            .build()

        return ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                setSeekParameters(SeekParameters.CLOSEST_SYNC)
                playbackParameters = PlaybackParameters(1.05f) // faster for lower latency
            }
    }

    fun createMediaItem(uri: Uri): MediaItem {
        return MediaItem.Builder()
            .setUri(uri)
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.05f).build()
            )
            .build()
    }

    fun createRtspMediaSource(mediaItem: MediaItem): RtspMediaSource {
        return RtspMediaSource.Factory()
            .setForceUseRtpTcp(true)
            .createMediaSource(mediaItem)
    }

    fun releaseExoPlayer() {
        synchronized(this) {
            exoPlayer?.release()
            exoPlayer = null
        }
    }
}
