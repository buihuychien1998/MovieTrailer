package com.example.mvvmmovieapp.ui.movie_trailer

import androidx.lifecycle.LiveData
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.MovieTrailerNetworkDataSource
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.MovieTrailer
import io.reactivex.disposables.CompositeDisposable

class MovieTrailerRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieTrailersNetworkDataSource: MovieTrailerNetworkDataSource

    fun fetchSingleMovieTrailer(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieTrailer> {

        movieTrailersNetworkDataSource =
            MovieTrailerNetworkDataSource(apiService, compositeDisposable)
        movieTrailersNetworkDataSource.fetchMovieTrailer(movieId)

        return movieTrailersNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieTrailerNetworkState(): LiveData<NetworkState> {
        return movieTrailersNetworkDataSource.networkState
    }


}