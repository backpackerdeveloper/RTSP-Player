package com.backpacker.rstpstream.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.backpacker.rstpstream.databinding.ActivityMainBinding
import com.backpacker.rstpstream.domain.model.StreamState
import com.backpacker.rstpstream.viewmodel.StreamViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: StreamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.initializePlayer(this)

        setupUI()
        observeStreamState()
    }

    private fun setupUI() {
        binding.apply {
            btnPlay.setOnClickListener { viewModel.startStreaming(etRtspUrl.text.toString().trim()) }
            btnPause.setOnClickListener { viewModel.pauseStreaming() }
            btnStop.setOnClickListener { viewModel.stopStreaming() }

            playerView.player = viewModel.exoPlayer
        }
    }

    private fun observeStreamState() {
        lifecycleScope.launch {
            viewModel.streamState.collectLatest { state ->
                binding.tvStatus.text = when (state) {
                    is StreamState.Loading -> "Loading..."
                    is StreamState.Playing -> "Streaming..."
                    is StreamState.Error -> "Error: ${state.message}"
                    is StreamState.Stopped -> "Stopped"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopStreaming() // to remove from memory
    }
}
