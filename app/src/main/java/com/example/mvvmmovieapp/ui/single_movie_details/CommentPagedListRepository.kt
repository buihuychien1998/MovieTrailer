package com.example.mvvmmovieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmmovieapp.data.api.POST_PER_PAGE
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.*
import com.example.mvvmmovieapp.data.vo.Comment
import com.example.mvvmmovieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class CommentPagedListRepository(private val apiService: TheMovieDBInterface) {

    lateinit var commentPagedList: LiveData<PagedList<Comment>>
    lateinit var commentsDataSourceFactory: CommentDataSourceFactory

    fun fetchLiveCommentPagedList(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<PagedList<Comment>> {
        commentsDataSourceFactory = CommentDataSourceFactory(apiService, compositeDisposable, movieId)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build()

        commentPagedList = LivePagedListBuilder(commentsDataSourceFactory, config).build()

        return commentPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<CommentDataSource, NetworkState>(
            commentsDataSourceFactory.commentsLiveDataSource, CommentDataSource::networkState
        )
    }
}