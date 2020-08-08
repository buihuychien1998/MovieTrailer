package com.example.mvvmmovieapp.ui.movie_trailer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.mvvmmovieapp.data.api.TheMovieDBClient
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.MovieTrailer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_movie_trailer.*
import kotlinx.android.synthetic.main.activity_single_movie.progress_bar
import kotlinx.android.synthetic.main.activity_single_movie.txt_error


const val API_KEY = "AIzaSyCkUiy5aQiWjR4RkM0LEWvf2qhNBj9QOrk"

class MovieTrailerActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieTrailerViewModel
    private lateinit var movieRepository: MovieTrailerRepository
    //    private val youTubePlayerFragmentX =
//        YouTubePlayerSupportFragmentX.newInstance()
    lateinit var simpleExoPlayer: SimpleExoPlayer
    var youtubeLink = "http://youtube.com/watch?v="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mvvmmovieapp.R.layout.activity_movie_trailer)
        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieTrailerRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieTrailer.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })


    }

    fun bindUI(it: MovieTrailer) {
        val videos = it.results
        try {
//            var check = false
//            val transaction = supportFragmentManager.beginTransaction()
            for (video in videos) {
                if (video.type == "Trailer") {
//                    check = true

//                    youTubePlayerFragmentX.retainInstance = true
//                    youTubePlayerFragmentX.initialize(
//                        API_KEY,
//                        object : YouTubePlayer.OnInitializedListener {
//                            override fun onInitializationSuccess(
//                                provider: YouTubePlayer.Provider?,
//                                player: YouTubePlayer,
//                                wasRestored: Boolean
//                            ) {
//                                if (!wasRestored) {
//                                    player.cueVideo(video.key)
//                                    player.setShowFullscreenButton(true)
//                                }
//                            }
//
//                            override fun onInitializationFailure(
//                                arg0: YouTubePlayer.Provider?,
//                                arg1: YouTubeInitializationResult?
//                            ) {
//                                Log.e("MovieTrailerActivity", arg1.toString())
//                            }
//                        })
//
//                    transaction.add(R.id.youtube_fragment, youTubePlayerFragmentX)
//                    transaction.commit()
//                    watchYoutubeVideo(this, video.key)


                    object : YouTubeExtractor(this) {
                        public override fun onExtractionComplete(
                            ytFiles: SparseArray<YtFile>?,
                            vMeta: VideoMeta
                        ) {
                            if (ytFiles != null) {
                                val itag = 22
                                val downloadUrl = ytFiles.get(itag).url
                                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(baseContext)
                                videoView.player = simpleExoPlayer
                                val dataSourceFactory = DefaultDataSourceFactory(
                                    baseContext, Util.getUserAgent(
                                        baseContext, getString(
                                            com.example.mvvmmovieapp.R.string.app_name
                                        )
                                    )
                                )
                                val uri = Uri.parse(downloadUrl)
                                val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                                    .createMediaSource(uri)
                                simpleExoPlayer.prepare(mediaSource)
                                simpleExoPlayer.playWhenReady = true
                                Log.d(MovieTrailerActivity::class.java.simpleName, downloadUrl)

                            }
                        }
                    }.extract(youtubeLink + video.key, true, true)


                    break
                }
            }
        } catch (ex: Exception) {
            Log.e("MovieTrailerActivity", ex.message.toString())
        }


    }


    private fun getViewModel(movieId: Int): MovieTrailerViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieTrailerViewModel(movieRepository, movieId) as T
            }
        })[MovieTrailerViewModel::class.java]
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()

    }

    private fun watchYoutubeVideo(context: Context, id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }
}
