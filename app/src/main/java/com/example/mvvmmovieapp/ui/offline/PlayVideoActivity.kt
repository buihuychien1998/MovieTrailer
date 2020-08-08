package com.example.mvvmmovieapp.ui.offline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.example.mvvmmovieapp.utils.Const.PATH
import kotlinx.android.synthetic.main.activity_play_video.*


class PlayVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mvvmmovieapp.R.layout.activity_play_video)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer(intent.getStringExtra(PATH))
    }

    private fun initializePlayer(path: String?) {
        videoView.setVideoPath(path)
        videoView.start()
        val mediaController = MediaController(this)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
    }

    override fun onStop() {
        super.onStop()
        if (videoView.isPlaying()){
            videoView.stopPlayback()
        }
    }

    override fun onBackPressed() {

        if (videoView.isPlaying()){
            videoView.stopPlayback()
            finish()
        }
        super.onBackPressed()
    }
}
