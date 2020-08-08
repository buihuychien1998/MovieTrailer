package com.example.mvvmmovieapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.vo.Comment
import io.reactivex.disposables.CompositeDisposable

class CommentDataSourceFactory(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val movieId: Int
) : DataSource.Factory<Int, Comment>() {

    val commentsLiveDataSource = MutableLiveData<CommentDataSource>()

    override fun create(): DataSource<Int, Comment> {
        val commentDataSource = CommentDataSource(apiService, compositeDisposable, movieId)

        commentsLiveDataSource.postValue(commentDataSource)
        return commentDataSource
    }
}