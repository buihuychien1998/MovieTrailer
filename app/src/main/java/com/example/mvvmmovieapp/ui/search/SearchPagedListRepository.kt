package com.example.mvvmmovieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmmovieapp.data.api.POST_PER_PAGE
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.*
import com.example.mvvmmovieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchPagedListRepository(private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: SearchDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable, query: String): LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = SearchDataSourceFactory(apiService, compositeDisposable, query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<SearchDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, SearchDataSource::networkState
        )
    }
}