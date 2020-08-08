package com.example.mvvmmovieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchActivityViewModel(val movieRepository : SearchPagedListRepository, var query: String) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val  moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable, query)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    fun clear(){
        movieRepository.moviesDataSourceFactory.moviesLiveDataSource.value?.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}