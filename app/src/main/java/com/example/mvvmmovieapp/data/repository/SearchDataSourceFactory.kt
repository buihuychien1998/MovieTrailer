package com.example.mvvmmovieapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable, val query: String)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = SearchDataSource(apiService,compositeDisposable, query)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}