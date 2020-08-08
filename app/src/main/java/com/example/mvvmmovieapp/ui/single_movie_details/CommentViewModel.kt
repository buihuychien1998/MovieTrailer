package com.example.mvvmmovieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.Comment
import io.reactivex.disposables.CompositeDisposable

class CommentViewModel(private val commentRepository: CommentPagedListRepository, movieId:Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val commentPageList: LiveData<PagedList<Comment>> by lazy {
        commentRepository.fetchLiveCommentPagedList(compositeDisposable, movieId)
    }
    val  networkState : LiveData<NetworkState> by lazy {
        commentRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return commentPageList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}