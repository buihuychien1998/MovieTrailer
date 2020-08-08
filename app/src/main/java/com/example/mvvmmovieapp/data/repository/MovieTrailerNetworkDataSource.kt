package com.example.mvvmmovieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.vo.MovieTrailer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieTrailerNetworkDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedMovieTrailerResponse = MutableLiveData<MovieTrailer>()
    val downloadedMovieResponse: LiveData<MovieTrailer>
        get() = _downloadedMovieTrailerResponse

    fun fetchMovieTrailer(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            compositeDisposable.add(
                apiService.getMovieTrailer(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieTrailerResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieTrailersDataSource", it.message)
                        }
                    )
            )

        } catch (e: Exception) {
            Log.e("MovieTrailersDataSource", e.message)
        }


    }
}