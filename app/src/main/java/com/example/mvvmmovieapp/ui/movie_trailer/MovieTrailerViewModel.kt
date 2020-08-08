package com.example.mvvmmovieapp.ui.movie_trailer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.MovieTrailer
import io.reactivex.disposables.CompositeDisposable

class MovieTrailerViewModel(private val movieRepository: MovieTrailerRepository, movieId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieTrailer: LiveData<MovieTrailer> by lazy {
        movieRepository.fetchSingleMovieTrailer(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieTrailerNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}