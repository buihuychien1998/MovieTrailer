package com.example.mvvmmovieapp.ui.single_movie_details

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmmovieapp.GlideApp
import com.example.mvvmmovieapp.R
import com.example.mvvmmovieapp.data.api.POSTER_BASE_URL
import com.example.mvvmmovieapp.data.api.TheMovieDBClient
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.DBHelper
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.MovieDetails
import com.example.mvvmmovieapp.ui.movie_trailer.MovieTrailerActivity
import com.example.mvvmmovieapp.utils.Const
import com.example.mvvmmovieapp.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_single_movie.*
import kotlinx.android.synthetic.main.activity_single_movie.btnBack
import java.text.NumberFormat
import java.util.*


class SingleMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var commentRepository: CommentPagedListRepository
    private lateinit var commentAdapter: CommentPagedListAdapter
    private lateinit var dbHelper: DBHelper
    lateinit var movieName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        setSupportActionBar(toolBar)
        dbHelper = DBHelper(this)
        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
        btnBack.setOnClickListener {
            super.onBackPressed()
        }
        btnTrailer.setOnClickListener {
            val intent = Intent(this, MovieTrailerActivity::class.java)
            intent.putExtra("id", movieId)
            startActivity(intent)
        }
        commentRepository = CommentPagedListRepository(apiService)
        commentViewModel = getCommentViewModel(movieId)
        commentAdapter = CommentPagedListAdapter(this)
        commentViewModel.commentPageList.observe(this, Observer {
            commentAdapter.submitList(it)
        })

        commentViewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (commentViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (commentViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!commentViewModel.listIsEmpty()) {
                commentAdapter.setNetworkState(it)
            }
        })
        rvComment.adapter = commentAdapter

        btnFavorite.setOnClickListener {
            val username = SharedPrefs.instance[Const.USERID, String::class.java, ""]
            if (TextUtils.isEmpty(username)){
                Toast.makeText(this, "You must login to save wish list", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    insertData(movieId.toString(), movieName, username)
                }catch (ex: Exception){

                }

            }

        }

    }

    fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.rating = it.rating.toFloat()
        movie_runtime.text = "${it.runtime} minutes"
        movie_overview.text = it.overview

        movieName = it.title

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        GlideApp.with(this)
            .load(moviePosterURL)
            .apply(
                RequestOptions().fitCenter().placeholder(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.poster_placeholder
                    )
                ).error(ContextCompat.getDrawable(baseContext, R.drawable.error))
            )
            .into(iv_movie_poster)

        iv_movie_poster.animation  = AnimationUtils.loadAnimation(this,R.anim.scale_animation)
    }


    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    private fun getCommentViewModel(movieId: Int): CommentViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CommentViewModel(commentRepository, movieId) as T
            }
        })[CommentViewModel::class.java]
    }

    private fun insertData(
        idMovie: String?,
        nameMovie: String?,
        idUser: String?
    ) {
        val resultInsert: Long = dbHelper.insertWishList(idMovie, nameMovie, idUser)
        if (resultInsert == -1L) {
            Toast.makeText(this, "Error insert!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Successful insert!", Toast.LENGTH_SHORT).show()
        }
    }
}
